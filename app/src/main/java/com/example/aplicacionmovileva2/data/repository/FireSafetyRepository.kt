package com.example.aplicacionmovileva2.data.repository

import com.example.aplicacionmovileva2.data.local.AppDatabaseHelper
import com.example.aplicacionmovileva2.data.model.ActivityItem
import com.example.aplicacionmovileva2.data.model.BuildingSummary
import com.example.aplicacionmovileva2.data.model.CredentialAuthRequest
import com.example.aplicacionmovileva2.data.model.CredentialAuthResponse
import com.example.aplicacionmovileva2.data.model.DoorData
import com.example.aplicacionmovileva2.data.model.DoorStatus
import com.example.aplicacionmovileva2.data.model.FireSensorData
import com.example.aplicacionmovileva2.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface FireSafetyRepository {
    fun getFireSensorData(): Flow<FireSensorData>
    fun getDoors(plantaCod: String? = null): Flow<List<DoorData>>
    fun getDoorById(id: String): DoorData?
    fun getBuildingSummary(): Flow<BuildingSummary>
    fun getActivityLogs(): Flow<List<ActivityItem>>
    fun updateDoorStatus(doorId: String, newStatus: DoorStatus, alerta: String = "Sin alertas"): Boolean
    fun updateFireSensorData(newData: FireSensorData)
    suspend fun registerUser(user: User): Result<Long>
    suspend fun authenticateUser(usernameOrRut: String, password: String): User?
    suspend fun resetPassword(identifier: String, newPassword: String): Result<Boolean>
    suspend fun updateUserProfile(userId: Long, nombre: String, email: String, newPassword: String?): Result<User>
    suspend fun validateCredential(request: CredentialAuthRequest): Result<CredentialAuthResponse>
}

/**
 * Repositorio persistente basado en base de datos SQLite (AppDatabaseHelper).
 * Mantiene la persistencia completa de usuarios registrados, puertas, telemetría
 * e historial de actividad en la base de datos local del dispositivo.
 */
class DatabaseFireSafetyRepository(private val dbHelper: AppDatabaseHelper) : FireSafetyRepository {

    private val _sensorData = MutableStateFlow(dbHelper.getFireSensorData())
    private val _doors = MutableStateFlow(dbHelper.getAllDoors())
    private val _buildingSummary = MutableStateFlow(dbHelper.getBuildingSummary())
    private val _activityLogs = MutableStateFlow(dbHelper.getActivityLogs())

    override fun getFireSensorData(): Flow<FireSensorData> = _sensorData.asStateFlow()

    override fun getDoors(plantaCod: String?): Flow<List<DoorData>> = _doors.asStateFlow()

    override fun getDoorById(id: String): DoorData? = dbHelper.getDoorById(id)

    override fun getBuildingSummary(): Flow<BuildingSummary> = _buildingSummary.asStateFlow()

    override fun getActivityLogs(): Flow<List<ActivityItem>> = _activityLogs.asStateFlow()

    override fun updateDoorStatus(doorId: String, newStatus: DoorStatus, alerta: String): Boolean {
        val success = dbHelper.updateDoorStatus(doorId, newStatus, alerta)
        if (success) {
            refreshData()
        }
        return success
    }

    override fun updateFireSensorData(newData: FireSensorData) {
        dbHelper.updateFireSensorData(newData)
        _sensorData.update { newData }
        refreshData()
    }

    override suspend fun registerUser(user: User): Result<Long> {
        val result = dbHelper.registerUser(user)
        if (result.isSuccess) {
            _activityLogs.update { dbHelper.getActivityLogs() }
            _buildingSummary.update { dbHelper.getBuildingSummary() }
        }
        return result
    }

    override suspend fun authenticateUser(usernameOrRut: String, password: String): User? {
        val user = dbHelper.authenticateUser(usernameOrRut, password)
        if (user != null) {
            dbHelper.addActivityLog(
                hora = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date()),
                titulo = "Inicio de sesión",
                detalle = "Usuario ${user.usuario} (${user.nombreApellido}) autenticado.",
                tipo = "USUARIO"
            )
            refreshData()
        }
        return user
    }

    override suspend fun resetPassword(identifier: String, newPassword: String): Result<Boolean> {
        val res = dbHelper.resetPassword(identifier, newPassword)
        if (res.isSuccess) {
            refreshData()
        }
        return res
    }

    override suspend fun updateUserProfile(userId: Long, nombre: String, email: String, newPassword: String?): Result<User> {
        val res = dbHelper.updateUserProfile(userId, nombre, email, newPassword)
        if (res.isSuccess) {
            refreshData()
        }
        return res
    }

    override suspend fun validateCredential(request: CredentialAuthRequest): Result<CredentialAuthResponse> {
        // Authenticate against SQLite users database
        val user = dbHelper.authenticateUser(request.usuarioORut, request.contrasena)
        return if (user != null) {
            // Update door state in database
            dbHelper.updateDoorStatus(
                request.puertaId,
                DoorStatus.ABIERTA,
                "Apertura autorizada por ${user.nombreApellido}"
            )
            refreshData()
            Result.success(
                CredentialAuthResponse(
                    autorizado = true,
                    mensaje = "Apertura autorizada de puerta ${request.puertaId} por ${user.nombreApellido}.",
                    token = "auth_token_jwt_${System.currentTimeMillis()}"
                )
            )
        } else {
            Result.failure(IllegalArgumentException("Credencial o contraseña incorrecta en la base de datos"))
        }
    }

    private fun refreshData() {
        _doors.update { dbHelper.getAllDoors() }
        _buildingSummary.update { dbHelper.getBuildingSummary() }
        _activityLogs.update { dbHelper.getActivityLogs() }
        _sensorData.update { dbHelper.getFireSensorData() }
    }
}
