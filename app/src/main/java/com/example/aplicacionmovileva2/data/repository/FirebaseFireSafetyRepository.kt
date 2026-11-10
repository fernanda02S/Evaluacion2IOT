package com.example.aplicacionmovileva2.data.repository

import com.example.aplicacionmovileva2.data.local.AppDatabaseHelper
import com.example.aplicacionmovileva2.data.model.ActivityItem
import com.example.aplicacionmovileva2.data.model.BuildingSummary
import com.example.aplicacionmovileva2.data.model.CredentialAuthRequest
import com.example.aplicacionmovileva2.data.model.CredentialAuthResponse
import com.example.aplicacionmovileva2.data.model.DoorData
import com.example.aplicacionmovileva2.data.model.DoorStatus
import com.example.aplicacionmovileva2.data.model.FireLevel
import com.example.aplicacionmovileva2.data.model.FireSensorData
import com.example.aplicacionmovileva2.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repositorio de datos utilizando Firebase Firestore como fuente de almacenamiento
 * principal y base de datos local SQLite para asegurar funcionamiento fluido y fuera de línea.
 */
class FirebaseFireSafetyRepository(
    private val dbHelper: AppDatabaseHelper,
    private val firestore: FirebaseFirestore? = try { FirebaseFirestore.getInstance() } catch (e: Exception) { null }
) : FireSafetyRepository {

    private val _sensorData = MutableStateFlow(dbHelper.getFireSensorData())
    private val _doors = MutableStateFlow(dbHelper.getAllDoors())
    private val _buildingSummary = MutableStateFlow(dbHelper.getBuildingSummary())
    private val _activityLogs = MutableStateFlow(dbHelper.getActivityLogs())

    init {
        setupFirestoreListeners()
    }

    private fun setupFirestoreListeners() {
        val fs = firestore ?: return

        try {
            // Prepopulate Firebase Firestore with initial doors if empty
            fs.collection("doors").get().addOnSuccessListener { snapshot ->
                if (snapshot.isEmpty) {
                    val localDoors = dbHelper.getAllDoors()
                    for (door in localDoors) {
                        saveDoorToFirestore(door)
                    }
                }
            }

            // Real-time listener for doors
            fs.collection("doors").addSnapshotListener { snapshot, e ->
                if (e == null && snapshot != null && !snapshot.isEmpty) {
                    val remoteDoors = snapshot.documents.mapNotNull { doc ->
                        val id = doc.id
                        val numPlano = doc.getString("numeroPlano") ?: "01"
                        val ubi = doc.getString("ubicacion") ?: ""
                        val planta = doc.getString("planta") ?: ""
                        val estadoStr = doc.getString("estado") ?: "Cerrada"
                        val estado = when (estadoStr.lowercase()) {
                            "abierta" -> DoorStatus.ABIERTA
                            "sin datos" -> DoorStatus.SIN_DATOS
                            else -> DoorStatus.CERRADA
                        }
                        val conectada = doc.getBoolean("conectada") ?: true
                        val detalleConexion = doc.getString("detalleConexion") ?: "Conectada"
                        val alerta = doc.getString("alerta") ?: "Sin alertas"

                        DoorData(id, numPlano, ubi, planta, estado, conectada, detalleConexion, alerta)
                    }
                    if (remoteDoors.isNotEmpty()) {
                        for (d in remoteDoors) {
                            dbHelper.updateDoorStatus(d.id, d.estado, d.alerta)
                        }
                        refreshData()
                    }
                }
            }

            // Real-time listener for Fire telemetry
            fs.collection("fire_telemetry").document("current").addSnapshotListener { snapshot, e ->
                if (e == null && snapshot != null && snapshot.exists()) {
                    val estado = snapshot.getString("estado") ?: "Normal"
                    val valor = snapshot.getLong("valorAnalogo")?.toInt() ?: 450
                    val ts = snapshot.getString("timestamp") ?: "09:41"
                    val nivelStr = snapshot.getString("nivel") ?: "BAJO"
                    val nivel = when (nivelStr.uppercase()) {
                        "BAJO" -> FireLevel.BAJO
                        "MEDIO" -> FireLevel.MEDIO
                        else -> FireLevel.ALTO
                    }
                    val desc = snapshot.getString("descripcionIntensidad") ?: "Normal"
                    val edif = snapshot.getString("edificio") ?: "Edificio Alameda"
                    val planta = snapshot.getString("planta") ?: "Planta 2"

                    val newData = FireSensorData(estado, valor, ts, nivel, desc, edif, planta, true)
                    dbHelper.updateFireSensorData(newData)
                    _sensorData.update { newData }
                }
            }
        } catch (_: Exception) {
            // Fallback gracefully to local SQLite
        }
    }

    override fun getFireSensorData(): Flow<FireSensorData> = _sensorData.asStateFlow()

    override fun getDoors(plantaCod: String?): Flow<List<DoorData>> = _doors.asStateFlow()

    override fun getDoorById(id: String): DoorData? = dbHelper.getDoorById(id)

    override fun getBuildingSummary(): Flow<BuildingSummary> = _buildingSummary.asStateFlow()

    override fun getActivityLogs(): Flow<List<ActivityItem>> = _activityLogs.asStateFlow()

    override fun updateDoorStatus(doorId: String, newStatus: DoorStatus, alerta: String): Boolean {
        val success = dbHelper.updateDoorStatus(doorId, newStatus, alerta)
        if (success) {
            val updatedDoor = dbHelper.getDoorById(doorId)
            if (updatedDoor != null) {
                saveDoorToFirestore(updatedDoor)
            }
            saveActivityToFirestore(
                ActivityItem(
                    hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                    titulo = "Estado de puerta $doorId actualizado",
                    detalle = "La puerta cambió a estado '${newStatus.label}'."
                )
            )
            refreshData()
        }
        return success
    }

    override fun updateFireSensorData(newData: FireSensorData) {
        dbHelper.updateFireSensorData(newData)
        _sensorData.update { newData }
        firestore?.let { fs ->
            try {
                val data = mapOf(
                    "estado" to newData.estado,
                    "valorAnalogo" to newData.valorAnalogo,
                    "timestamp" to newData.timestamp,
                    "nivel" to newData.nivel.name,
                    "descripcionIntensidad" to newData.descripcionIntensidad,
                    "edificio" to newData.edificio,
                    "planta" to newData.planta
                )
                fs.collection("fire_telemetry").document("current").set(data, SetOptions.merge())
            } catch (_: Exception) {}
        }
        refreshData()
    }

    override suspend fun registerUser(user: User): Result<Long> {
        val result = dbHelper.registerUser(user)
        if (result.isSuccess) {
            firestore?.let { fs ->
                try {
                    val userData = mapOf(
                        "email" to user.email.lowercase(),
                        "rut" to user.rut,
                        "nombreApellido" to user.nombreApellido,
                        "usuario" to user.usuario,
                        "contrasena" to user.contrasena,
                        "fechaRegistro" to user.fechaRegistro
                    )
                    fs.collection("users").document(user.usuario).set(userData, SetOptions.merge())
                } catch (_: Exception) {}
            }
            saveActivityToFirestore(
                ActivityItem(
                    hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                    titulo = "Nuevo usuario registrado",
                    detalle = "Usuario ${user.usuario} (${user.nombreApellido}) creado."
                )
            )
            refreshData()
        }
        return result
    }

    override suspend fun authenticateUser(usernameOrRut: String, password: String): User? {
        val user = dbHelper.authenticateUser(usernameOrRut, password)
        if (user != null) {
            dbHelper.addActivityLog(
                hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                titulo = "Inicio de sesión",
                detalle = "Usuario ${user.usuario} autenticado en el sistema.",
                tipo = "USUARIO"
            )
            saveActivityToFirestore(
                ActivityItem(
                    hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                    titulo = "Inicio de sesión",
                    detalle = "Usuario ${user.usuario} autenticado en el sistema."
                )
            )
            refreshData()
        }
        return user
    }

    override suspend fun resetPassword(identifier: String, newPassword: String): Result<Boolean> {
        val res = dbHelper.resetPassword(identifier, newPassword)
        if (res.isSuccess) {
            firestore?.let { fs ->
                try {
                    fs.collection("users").document(identifier).update("contrasena", newPassword)
                } catch (_: Exception) {}
            }
            saveActivityToFirestore(
                ActivityItem(
                    hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                    titulo = "Restablecimiento de contraseña",
                    detalle = "Contraseña actualizada para $identifier."
                )
            )
            refreshData()
        }
        return res
    }

    override suspend fun updateUserProfile(userId: Long, nombre: String, email: String, newPassword: String?): Result<User> {
        val res = dbHelper.updateUserProfile(userId, nombre, email, newPassword)
        if (res.isSuccess) {
            val updatedUser = res.getOrNull()
            if (updatedUser != null && firestore != null) {
                try {
                    val map = mutableMapOf<String, Any>(
                        "nombreApellido" to nombre,
                        "email" to email
                    )
                    if (!newPassword.isNullOrBlank()) map["contrasena"] = newPassword
                    firestore.collection("users").document(updatedUser.usuario).set(map, SetOptions.merge())
                } catch (_: Exception) {}
            }
            refreshData()
        }
        return res
    }

    override suspend fun validateCredential(request: CredentialAuthRequest): Result<CredentialAuthResponse> {
        val user = dbHelper.authenticateUser(request.usuarioORut, request.contrasena)
        return if (user != null) {
            dbHelper.updateDoorStatus(
                request.puertaId,
                DoorStatus.ABIERTA,
                "Apertura autorizada por ${user.nombreApellido}"
            )
            val updatedDoor = dbHelper.getDoorById(request.puertaId)
            if (updatedDoor != null) {
                saveDoorToFirestore(updatedDoor)
            }
            saveActivityToFirestore(
                ActivityItem(
                    hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                    titulo = "Apertura autorizada",
                    detalle = "Puerta ${request.puertaId} abierta por ${user.nombreApellido}."
                )
            )
            refreshData()
            Result.success(
                CredentialAuthResponse(
                    autorizado = true,
                    mensaje = "Apertura autorizada de puerta ${request.puertaId} por ${user.nombreApellido}.",
                    token = "firebase_auth_token_${System.currentTimeMillis()}"
                )
            )
        } else {
            Result.failure(IllegalArgumentException("Usuario o contraseña incorrectos"))
        }
    }

    private fun saveDoorToFirestore(door: DoorData) {
        val fs = firestore ?: return
        try {
            val map = mapOf(
                "id" to door.id,
                "numeroPlano" to door.numeroPlano,
                "ubicacion" to door.ubicacion,
                "planta" to door.planta,
                "estado" to door.estado.label,
                "conectada" to door.conectada,
                "detalleConexion" to door.detalleConexion,
                "alerta" to door.alerta
            )
            fs.collection("doors").document(door.id).set(map, SetOptions.merge())
        } catch (_: Exception) {}
    }

    private fun saveActivityToFirestore(activity: ActivityItem) {
        val fs = firestore ?: return
        try {
            val map = mapOf(
                "hora" to activity.hora,
                "titulo" to activity.titulo,
                "detalle" to activity.detalle,
                "timestamp" to System.currentTimeMillis()
            )
            fs.collection("activity_log").add(map)
        } catch (_: Exception) {}
    }

    private fun refreshData() {
        _doors.update { dbHelper.getAllDoors() }
        _buildingSummary.update { dbHelper.getBuildingSummary() }
        _activityLogs.update { dbHelper.getActivityLogs() }
        _sensorData.update { dbHelper.getFireSensorData() }
    }
}
