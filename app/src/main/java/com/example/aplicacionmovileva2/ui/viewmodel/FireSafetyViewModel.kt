package com.example.aplicacionmovileva2.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionmovileva2.data.local.AppDatabaseHelper
import com.example.aplicacionmovileva2.data.model.CredentialAuthRequest
import com.example.aplicacionmovileva2.data.model.DoorData
import com.example.aplicacionmovileva2.data.model.DoorStatus
import com.example.aplicacionmovileva2.data.repository.FirebaseFireSafetyRepository
import com.example.aplicacionmovileva2.data.repository.FireSafetyRepository
import com.example.aplicacionmovileva2.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FireSafetyViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper: AppDatabaseHelper = AppDatabaseHelper.getInstance(application)
    private val repository: FireSafetyRepository = FirebaseFireSafetyRepository(dbHelper)

    private val _uiState = MutableStateFlow(FireSafetyUiState())
    val uiState: StateFlow<FireSafetyUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getFireSensorData().collect { sensor ->
                _uiState.update { it.copy(fireSensor = sensor) }
            }
        }
        viewModelScope.launch {
            repository.getBuildingSummary().collect { summary ->
                _uiState.update { it.copy(buildingSummary = summary) }
            }
        }
        viewModelScope.launch {
            repository.getDoors().collect { doorsList ->
                _uiState.update { state ->
                    state.copy(
                        doors = doorsList,
                        filteredDoors = filterDoors(
                            doorsList,
                            state.doorSearchQuery,
                            state.selectedDoorFloorFilter,
                            state.selectedDoorStatusFilter
                        )
                    )
                }
            }
        }
        viewModelScope.launch {
            repository.getActivityLogs().collect { logs ->
                _uiState.update { it.copy(activityLogs = logs) }
            }
        }
    }

    // --- Navigation ---

    fun navigateTo(screen: Screen) {
        _uiState.update { it.copy(previousScreen = it.currentScreen, currentScreen = screen) }
    }

    fun navigateBack() {
        val prev = _uiState.value.previousScreen ?: Screen.RESUMEN
        _uiState.update { it.copy(currentScreen = prev, previousScreen = null) }
    }

    // --- Login ---

    fun onLoginUserOrRutChange(value: String) {
        _uiState.update { it.copy(loginUserOrRut = value, loginErrorMessage = null) }
    }

    fun onLoginPasswordChange(value: String) {
        _uiState.update { it.copy(loginPassword = value, loginErrorMessage = null) }
    }

    fun login() {
        val usernameOrRut = _uiState.value.loginUserOrRut.trim()
        val password = _uiState.value.loginPassword

        if (usernameOrRut.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(loginErrorMessage = "Ingresa tu usuario o RUT y contraseña") }
            return
        }

        viewModelScope.launch {
            val user = repository.authenticateUser(usernameOrRut, password)
            if (user != null) {
                _uiState.update {
                    it.copy(
                        currentUser = user,
                        loginErrorMessage = null,
                        loginSuccessNotification = null,
                        loginPassword = "",
                        currentScreen = Screen.RESUMEN,
                        previousScreen = null
                    )
                }
            } else {
                _uiState.update { it.copy(loginErrorMessage = "Usuario o contraseña incorrectos") }
            }
        }
    }

    // --- Register ---

    fun onRegisterEmailChange(value: String) = _uiState.update { it.copy(registerEmail = value, registerErrorMessage = null) }
    fun onRegisterRutChange(value: String) = _uiState.update { it.copy(registerRut = value, registerErrorMessage = null) }
    fun onRegisterNombreChange(value: String) = _uiState.update { it.copy(registerNombreApellido = value, registerErrorMessage = null) }
    fun onRegisterUsuarioChange(value: String) = _uiState.update { it.copy(registerUsuario = value, registerErrorMessage = null) }
    fun onRegisterPasswordChange(value: String) = _uiState.update { it.copy(registerPassword = value, registerErrorMessage = null) }

    fun register() {
        val state = _uiState.value
        val email = state.registerEmail.trim()
        val rut = state.registerRut.trim()
        val nombre = state.registerNombreApellido.trim()
        val usuario = state.registerUsuario.trim()
        val pass = state.registerPassword

        if (email.isBlank() || !email.contains("@")) {
            _uiState.update { it.copy(registerErrorMessage = "Ingresa un correo electrónico válido") }
            return
        }
        if (rut.isBlank()) {
            _uiState.update { it.copy(registerErrorMessage = "Ingresa tu RUT") }
            return
        }
        if (nombre.isBlank()) {
            _uiState.update { it.copy(registerErrorMessage = "Ingresa tu nombre y apellido") }
            return
        }
        if (usuario.isBlank()) {
            _uiState.update { it.copy(registerErrorMessage = "Elige un nombre de usuario") }
            return
        }
        if (pass.length < 8) {
            _uiState.update { it.copy(registerErrorMessage = "La contraseña debe tener al menos 8 caracteres") }
            return
        }

        _uiState.update { it.copy(registerLoading = true, registerErrorMessage = null) }

        viewModelScope.launch {
            val user = com.example.aplicacionmovileva2.data.model.User(
                email = email,
                rut = rut,
                nombreApellido = nombre,
                usuario = usuario,
                contrasena = pass
            )
            val result = repository.registerUser(user)
            _uiState.update { it.copy(registerLoading = false) }

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        loginUserOrRut = usuario,
                        loginPassword = "",
                        loginSuccessNotification = "Cuenta creada exitosamente. Inicia sesión con tu usuario y contraseña.",
                        registerEmail = "",
                        registerRut = "",
                        registerNombreApellido = "",
                        registerUsuario = "",
                        registerPassword = "",
                        registerErrorMessage = null,
                        currentScreen = Screen.LOGIN
                    )
                }
            }.onFailure { err ->
                val msg = when {
                    err.message?.contains("UNIQUE", ignoreCase = true) == true ->
                        "El correo, RUT o usuario ya está registrado"
                    else -> err.message ?: "No se pudo crear la cuenta"
                }
                _uiState.update { it.copy(registerErrorMessage = msg) }
            }
        }
    }

    // --- Recover Password ---

    fun onRecoverIdentifierChange(value: String) = _uiState.update { it.copy(recoverIdentifier = value, recoverErrorMessage = null) }
    fun onRecoverNewPasswordChange(value: String) = _uiState.update { it.copy(recoverNewPassword = value, recoverErrorMessage = null) }

    fun resetPassword() {
        val id = _uiState.value.recoverIdentifier.trim()
        val pass = _uiState.value.recoverNewPassword

        if (id.isBlank()) {
            _uiState.update { it.copy(recoverErrorMessage = "Ingresa tu usuario, RUT o correo") }
            return
        }
        if (pass.length < 8) {
            _uiState.update { it.copy(recoverErrorMessage = "La nueva contraseña debe tener al menos 8 caracteres") }
            return
        }

        viewModelScope.launch {
            val result = repository.resetPassword(id, pass)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        recoverSuccessMessage = "Contraseña restablecida con éxito.",
                        loginUserOrRut = id,
                        loginPassword = "",
                        loginSuccessNotification = "Contraseña actualizada. Inicia sesión con tu nueva contraseña."
                    )
                }
            } else {
                _uiState.update { it.copy(recoverErrorMessage = "No se encontró ningún usuario con los datos ingresados.") }
            }
        }
    }

    // --- Profile Management & Logout ---

    fun openProfile() {
        val user = _uiState.value.currentUser
        _uiState.update {
            it.copy(
                profileNombre = user?.nombreApellido ?: "",
                profileEmail = user?.email ?: "",
                profileNewPassword = "",
                profileEditMode = false,
                profileErrorMessage = null,
                profileSuccessMessage = null,
                previousScreen = it.currentScreen,
                currentScreen = Screen.PROFILE
            )
        }
    }

    fun toggleProfileEditMode() {
        _uiState.update {
            it.copy(
                profileEditMode = !it.profileEditMode,
                profileErrorMessage = null,
                profileSuccessMessage = null
            )
        }
    }

    fun onProfileNombreChange(v: String) = _uiState.update { it.copy(profileNombre = v, profileErrorMessage = null) }
    fun onProfileEmailChange(v: String) = _uiState.update { it.copy(profileEmail = v, profileErrorMessage = null) }
    fun onProfileNewPasswordChange(v: String) = _uiState.update { it.copy(profileNewPassword = v, profileErrorMessage = null) }

    fun saveProfileChanges() {
        val state = _uiState.value
        val user = state.currentUser
        if (user == null) {
            _uiState.update { it.copy(profileErrorMessage = "No hay usuario activo") }
            return
        }

        val nombre = state.profileNombre.trim()
        val email = state.profileEmail.trim()
        val newPass: String? = state.profileNewPassword.ifBlank { null }

        if (nombre.isBlank()) {
            _uiState.update { it.copy(profileErrorMessage = "El nombre no puede estar vacío") }
            return
        }
        if (email.isBlank() || !email.contains("@")) {
            _uiState.update { it.copy(profileErrorMessage = "Ingresa un correo electrónico válido") }
            return
        }
        if (newPass != null && newPass.length < 8) {
            _uiState.update { it.copy(profileErrorMessage = "La nueva contraseña debe tener al menos 8 caracteres") }
            return
        }

        _uiState.update { it.copy(profileLoading = true, profileErrorMessage = null) }

        viewModelScope.launch {
            val result = repository.updateUserProfile(user.id, nombre, email, newPass)
            _uiState.update { it.copy(profileLoading = false) }

            result.onSuccess { updatedUser ->
                _uiState.update {
                    it.copy(
                        currentUser = updatedUser,
                        profileEditMode = false,
                        profileNewPassword = "",
                        profileSuccessMessage = "Tus datos personales han sido actualizados en la base de datos."
                    )
                }
            }.onFailure { err ->
                _uiState.update { it.copy(profileErrorMessage = err.message ?: "Error al actualizar los datos") }
            }
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                currentUser = null,
                loginPassword = "",
                loginErrorMessage = null,
                loginSuccessNotification = "Has cerrado sesión correctamente.",
                currentScreen = Screen.LOGIN,
                previousScreen = null
            )
        }
    }

    // --- Floor Plan & Doors ---

    fun selectFloor(floorIndex: Int) {
        _uiState.update { it.copy(selectedFloorIndex = floorIndex) }
    }

    fun selectDoorOnPlan(doorId: String) {
        _uiState.update { it.copy(selectedDoorIdOnPlan = doorId) }
    }

    fun openDoorDetail(door: DoorData) {
        _uiState.update {
            it.copy(
                selectedDoorForDetail = door,
                previousScreen = it.currentScreen,
                currentScreen = Screen.DOOR_DETAIL
            )
        }
    }

    fun toggleDoorStateFromDetail(door: DoorData) {
        val newStatus = if (door.estado == DoorStatus.CERRADA) DoorStatus.ABIERTA else DoorStatus.CERRADA
        val alerta = if (newStatus == DoorStatus.ABIERTA) "Alerta de cierre" else "Sin alertas"
        viewModelScope.launch {
            repository.updateDoorStatus(door.id, newStatus, alerta)
            val updatedDoor = door.copy(estado = newStatus, alerta = alerta)
            _uiState.update { it.copy(selectedDoorForDetail = updatedDoor) }
        }
    }

    fun updateDoorSearchQuery(query: String) {
        _uiState.update { state ->
            state.copy(
                doorSearchQuery = query,
                filteredDoors = filterDoors(state.doors, query, state.selectedDoorFloorFilter, state.selectedDoorStatusFilter)
            )
        }
    }

    fun setDoorFloorFilter(floor: String) {
        _uiState.update { state ->
            state.copy(
                selectedDoorFloorFilter = floor,
                filteredDoors = filterDoors(state.doors, state.doorSearchQuery, floor, state.selectedDoorStatusFilter)
            )
        }
    }

    fun setDoorStatusFilter(status: String) {
        _uiState.update { state ->
            state.copy(
                selectedDoorStatusFilter = status,
                filteredDoors = filterDoors(state.doors, state.doorSearchQuery, state.selectedDoorFloorFilter, status)
            )
        }
    }

    private fun filterDoors(
        doors: List<DoorData>,
        query: String,
        floorFilter: String,
        statusFilter: String
    ): List<DoorData> {
        return doors.filter { door ->
            val matchesFloor = when (floorFilter) {
                "Todas" -> true
                "Planta baja" -> door.id.startsWith("PB")
                "Planta 1" -> door.id.startsWith("P1")
                "Planta 2" -> door.id.startsWith("P2")
                "Planta 3" -> door.id.startsWith("P3")
                else -> true
            }
            val matchesStatus = when (statusFilter) {
                "todos" -> true
                "abierta" -> door.estado == DoorStatus.ABIERTA
                "cerrada" -> door.estado == DoorStatus.CERRADA
                "sin datos" -> door.estado == DoorStatus.SIN_DATOS
                else -> true
            }
            val matchesQuery = query.isBlank() ||
                    door.id.contains(query, ignoreCase = true) ||
                    door.ubicacion.contains(query, ignoreCase = true) ||
                    door.planta.contains(query, ignoreCase = true)
            matchesFloor && matchesStatus && matchesQuery
        }.sortedWith(
            compareByDescending<DoorData> { it.estado == DoorStatus.ABIERTA }
                .thenByDescending { it.estado == DoorStatus.SIN_DATOS }
        )
    }

    // --- Credential ---

    fun onCredentialRutOrUserChange(value: String) = _uiState.update { it.copy(credentialRutOrUser = value, credentialErrorMessage = null) }
    fun onCredentialPasswordChange(value: String) = _uiState.update { it.copy(credentialPassword = value, credentialErrorMessage = null) }

    fun validateCredentialAndUnlock(doorId: String) {
        val rutOrUser = _uiState.value.credentialRutOrUser.trim()
        val password = _uiState.value.credentialPassword

        if (rutOrUser.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(credentialErrorMessage = "Ingresa tu usuario/RUT y contraseña") }
            return
        }

        _uiState.update { it.copy(credentialLoading = true, credentialErrorMessage = null) }

        viewModelScope.launch {
            val request = CredentialAuthRequest(usuarioORut = rutOrUser, contrasena = password, puertaId = doorId)
            val result = repository.validateCredential(request)
            _uiState.update { it.copy(credentialLoading = false) }

            result.onSuccess { response ->
                _uiState.update {
                    it.copy(
                        credentialRutOrUser = "",
                        credentialPassword = "",
                        messageDialogText = response.mensaje
                    )
                }
            }.onFailure { err ->
                _uiState.update { it.copy(credentialErrorMessage = err.message ?: "Credencial incorrecta") }
            }
        }
    }

    // --- Dialogs ---

    fun showEmergencyDialog(show: Boolean) {
        _uiState.update { it.copy(emergencyCallDialogVisible = show) }
    }

    fun dismissMessageDialog() {
        _uiState.update { it.copy(messageDialogText = null) }
    }
}
