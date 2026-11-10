package com.example.aplicacionmovileva2.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.aplicacionmovileva2.data.model.ActivityItem
import com.example.aplicacionmovileva2.data.model.BuildingSummary
import com.example.aplicacionmovileva2.data.model.DoorData
import com.example.aplicacionmovileva2.data.model.DoorStatus
import com.example.aplicacionmovileva2.data.model.FireLevel
import com.example.aplicacionmovileva2.data.model.FireSensorData
import com.example.aplicacionmovileva2.data.model.FloorSummary
import com.example.aplicacionmovileva2.data.model.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "fire_safety_iot.db"
        const val DATABASE_VERSION = 1

        // Users table
        const val TABLE_USERS = "users"
        const val COL_USER_ID = "id"
        const val COL_USER_EMAIL = "email"
        const val COL_USER_RUT = "rut"
        const val COL_USER_NOMBRE = "nombre_apellido"
        const val COL_USER_USUARIO = "usuario"
        const val COL_USER_PASSWORD = "password"
        const val COL_USER_FECHA = "fecha_registro"

        // Doors table
        const val TABLE_DOORS = "doors"
        const val COL_DOOR_ID = "id"
        const val COL_DOOR_NUM_PLANO = "numero_plano"
        const val COL_DOOR_UBICACION = "ubicacion"
        const val COL_DOOR_PLANTA = "planta"
        const val COL_DOOR_PLANTA_COD = "planta_cod"
        const val COL_DOOR_ESTADO = "estado"
        const val COL_DOOR_CONECTADA = "conectada"
        const val COL_DOOR_DETALLE_CONEXION = "detalle_conexion"
        const val COL_DOOR_ALERTA = "alerta"

        // Activity log table
        const val TABLE_ACTIVITY = "activity_log"
        const val COL_ACT_ID = "id"
        const val COL_ACT_HORA = "hora"
        const val COL_ACT_TITULO = "titulo"
        const val COL_ACT_DETALLE = "detalle"
        const val COL_ACT_TIPO = "tipo"

        // Fire telemetry table
        const val TABLE_FIRE = "fire_telemetry"
        const val COL_FIRE_ID = "id"
        const val COL_FIRE_ESTADO = "estado"
        const val COL_FIRE_VALOR = "valor_analogo"
        const val COL_FIRE_TIMESTAMP = "timestamp"
        const val COL_FIRE_NIVEL = "nivel"
        const val COL_FIRE_DESC = "descripcion"
        const val COL_FIRE_EDIFICIO = "edificio"
        const val COL_FIRE_PLANTA = "planta"

        @Volatile
        private var INSTANCE: AppDatabaseHelper? = null

        fun getInstance(context: Context): AppDatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppDatabaseHelper(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_USERS (
                $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER_EMAIL TEXT UNIQUE,
                $COL_USER_RUT TEXT UNIQUE,
                $COL_USER_NOMBRE TEXT,
                $COL_USER_USUARIO TEXT UNIQUE,
                $COL_USER_PASSWORD TEXT,
                $COL_USER_FECHA TEXT
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_DOORS (
                $COL_DOOR_ID TEXT PRIMARY KEY,
                $COL_DOOR_NUM_PLANO TEXT,
                $COL_DOOR_UBICACION TEXT,
                $COL_DOOR_PLANTA TEXT,
                $COL_DOOR_PLANTA_COD TEXT,
                $COL_DOOR_ESTADO TEXT,
                $COL_DOOR_CONECTADA INTEGER,
                $COL_DOOR_DETALLE_CONEXION TEXT,
                $COL_DOOR_ALERTA TEXT
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_ACTIVITY (
                $COL_ACT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ACT_HORA TEXT,
                $COL_ACT_TITULO TEXT,
                $COL_ACT_DETALLE TEXT,
                $COL_ACT_TIPO TEXT
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_FIRE (
                $COL_FIRE_ID INTEGER PRIMARY KEY,
                $COL_FIRE_ESTADO TEXT,
                $COL_FIRE_VALOR INTEGER,
                $COL_FIRE_TIMESTAMP TEXT,
                $COL_FIRE_NIVEL TEXT,
                $COL_FIRE_DESC TEXT,
                $COL_FIRE_EDIFICIO TEXT,
                $COL_FIRE_PLANTA TEXT
            )
            """.trimIndent()
        )

        prepopulateDefaultData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DOORS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTIVITY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FIRE")
        onCreate(db)
    }

    private fun prepopulateDefaultData(db: SQLiteDatabase) {
        // Pre-register default demo user
        insertUserInternal(db, "laura.martin@seguridad.cl", "12.345.678-9", "Laura Martin", "admin", "admin123", "27 oct 2026")
        insertUserInternal(db, "operador@seguridad.cl", "11.222.333-4", "Operador Seguridad", "operador", "segura2026", "27 oct 2026")

        // Prepopulate Doors for all 4 floors (PB, P1, P2, P3) - 6 doors each = 24 doors
        val floors = listOf(
            Triple("PB", "Planta baja", "Baja"),
            Triple("P1", "Planta 1", "Planta 1"),
            Triple("P2", "Planta 2", "Planta 2"),
            Triple("P3", "Planta 3", "Planta 3")
        )

        val doorTemplates = listOf(
            Triple("01", "Oficinas A", "Cerrada"),
            Triple("02", "Sala técnica", "Cerrada"),
            Triple("03", "Escalera B", "Cerrada"),
            Triple("04", "Escalera A", "Abierta"), // P2-04 will be abierta
            Triple("05", "Oficinas B", "Cerrada"),
            Triple("06", "Archivo", "Sin datos")    // P2-06 will be sin datos
        )

        floors.forEach { (floorCod, floorNombre, _) ->
            doorTemplates.forEach { (numPlano, ubicacion, defEstado) ->
                val doorId = "$floorCod-$numPlano"
                val estado = if (floorCod == "P2") {
                    when (numPlano) {
                        "04" -> "Abierta"
                        "06" -> "Sin datos"
                        else -> "Cerrada"
                    }
                } else {
                    "Cerrada"
                }

                val conectada = !(floorCod == "P2" && numPlano == "06")
                val detalleConexion = if (conectada) "Conectada" else "Sin conexión - 09:37"
                val alerta = when {
                    estado == "Abierta" -> "Alerta de cierre"
                    estado == "Sin datos" -> "Sin telemetría"
                    else -> "Sin alertas"
                }

                insertDoorInternal(
                    db = db,
                    id = doorId,
                    numeroPlano = numPlano,
                    ubicacion = ubicacion,
                    planta = floorNombre,
                    plantaCod = floorCod,
                    estado = estado,
                    conectada = if (conectada) 1 else 0,
                    detalleConexion = detalleConexion,
                    alerta = alerta
                )
            }
        }

        // Prepopulate initial activity logs
        insertActivityInternal(db, "09:40", "P2-04 sigue abierta", "Verificación asignada a Laura Martin.", "ALERTA")
        insertActivityInternal(db, "09:38", "P2-04 alerta de cierre", "Puerta abierta detectada en Planta 2.", "ALERTA")
        insertActivityInternal(db, "09:37", "P2-06 pérdida de telemetría", "Sensor sin conexión en Archivo.", "SISTEMA")
        insertActivityInternal(db, "09:30", "Ronda matutina completada", "Planta 1 y Planta 3 sin novedades.", "RONDA")
        insertActivityInternal(db, "09:15", "Sistema inicializado", "Supervisión de 24 puertas activada.", "SISTEMA")

        // Prepopulate Fire telemetry
        val cvFire = ContentValues().apply {
            put(COL_FIRE_ID, 1)
            put(COL_FIRE_ESTADO, "Alerta")
            put(COL_FIRE_VALOR, 720)
            put(COL_FIRE_TIMESTAMP, "2026-10-27T09:41:00Z")
            put(COL_FIRE_NIVEL, "ALTO")
            put(COL_FIRE_DESC, "Intensidad de llama alta")
            put(COL_FIRE_EDIFICIO, "Edificio Alameda")
            put(COL_FIRE_PLANTA, "Planta 2")
        }
        db.insert(TABLE_FIRE, null, cvFire)
    }

    private fun insertUserInternal(
        db: SQLiteDatabase,
        email: String,
        rut: String,
        nombre: String,
        usuario: String,
        pass: String,
        fecha: String
    ) {
        val cv = ContentValues().apply {
            put(COL_USER_EMAIL, email)
            put(COL_USER_RUT, rut)
            put(COL_USER_NOMBRE, nombre)
            put(COL_USER_USUARIO, usuario)
            put(COL_USER_PASSWORD, pass)
            put(COL_USER_FECHA, fecha)
        }
        db.insert(TABLE_USERS, null, cv)
    }

    private fun insertDoorInternal(
        db: SQLiteDatabase,
        id: String,
        numeroPlano: String,
        ubicacion: String,
        planta: String,
        plantaCod: String,
        estado: String,
        conectada: Int,
        detalleConexion: String,
        alerta: String
    ) {
        val cv = ContentValues().apply {
            put(COL_DOOR_ID, id)
            put(COL_DOOR_NUM_PLANO, numeroPlano)
            put(COL_DOOR_UBICACION, ubicacion)
            put(COL_DOOR_PLANTA, planta)
            put(COL_DOOR_PLANTA_COD, plantaCod)
            put(COL_DOOR_ESTADO, estado)
            put(COL_DOOR_CONECTADA, conectada)
            put(COL_DOOR_DETALLE_CONEXION, detalleConexion)
            put(COL_DOOR_ALERTA, alerta)
        }
        db.insert(TABLE_DOORS, null, cv)
    }

    private fun insertActivityInternal(db: SQLiteDatabase, hora: String, titulo: String, detalle: String, tipo: String) {
        val cv = ContentValues().apply {
            put(COL_ACT_HORA, hora)
            put(COL_ACT_TITULO, titulo)
            put(COL_ACT_DETALLE, detalle)
            put(COL_ACT_TIPO, tipo)
        }
        db.insert(TABLE_ACTIVITY, null, cv)
    }

    // --- User & Auth DAO ---

    fun registerUser(user: User): Result<Long> {
        val db = writableDatabase
        return try {
            val fecha = if (user.fechaRegistro.isBlank()) {
                SimpleDateFormat("dd MMM yyyy", Locale("es", "CL")).format(Date())
            } else user.fechaRegistro

            val cv = ContentValues().apply {
                put(COL_USER_EMAIL, user.email.trim().lowercase())
                put(COL_USER_RUT, user.rut.trim())
                put(COL_USER_NOMBRE, user.nombreApellido.trim())
                put(COL_USER_USUARIO, user.usuario.trim())
                put(COL_USER_PASSWORD, user.contrasena)
                put(COL_USER_FECHA, fecha)
            }
            val id = db.insertOrThrow(TABLE_USERS, null, cv)
            if (id > 0) {
                addActivityLog(
                    hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                    titulo = "Nuevo usuario registrado",
                    detalle = "Usuario ${user.usuario} (${user.nombreApellido}) creado en la app.",
                    tipo = "USUARIO"
                )
                Result.success(id)
            } else {
                Result.failure(Exception("No se pudo registrar el usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun authenticateUser(usernameOrRut: String, password: String): User? {
        val db = readableDatabase
        val query = """
            SELECT * FROM $TABLE_USERS 
            WHERE (LOWER($COL_USER_USUARIO) = ? OR $COL_USER_RUT = ?)
            AND $COL_USER_PASSWORD = ?
        """.trimIndent()
        val cursor: Cursor = db.rawQuery(query, arrayOf(usernameOrRut.trim().lowercase(), usernameOrRut.trim(), password))
        return cursor.use {
            if (it.moveToFirst()) {
                User(
                    id = it.getLong(it.getColumnIndexOrThrow(COL_USER_ID)),
                    email = it.getString(it.getColumnIndexOrThrow(COL_USER_EMAIL)),
                    rut = it.getString(it.getColumnIndexOrThrow(COL_USER_RUT)),
                    nombreApellido = it.getString(it.getColumnIndexOrThrow(COL_USER_NOMBRE)),
                    usuario = it.getString(it.getColumnIndexOrThrow(COL_USER_USUARIO)),
                    contrasena = it.getString(it.getColumnIndexOrThrow(COL_USER_PASSWORD)),
                    fechaRegistro = it.getString(it.getColumnIndexOrThrow(COL_USER_FECHA))
                )
            } else {
                null
            }
        }
    }

    fun resetPassword(identifier: String, newPassword: String): Result<Boolean> {
        val db = writableDatabase
        return try {
            val cv = ContentValues().apply {
                put(COL_USER_PASSWORD, newPassword)
            }
            val rows = db.update(
                TABLE_USERS,
                cv,
                "LOWER($COL_USER_USUARIO) = ? OR $COL_USER_RUT = ? OR LOWER($COL_USER_EMAIL) = ?",
                arrayOf(identifier.trim().lowercase(), identifier.trim(), identifier.trim().lowercase())
            )
            if (rows > 0) {
                addActivityLog(
                    hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                    titulo = "Restablecimiento de contraseña",
                    detalle = "Contraseña actualizada para $identifier.",
                    tipo = "SEGURIDAD"
                )
                Result.success(true)
            } else {
                Result.failure(Exception("No se encontró ningún usuario con ese RUT o correo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun updateUserProfile(userId: Long, nombre: String, email: String, newPassword: String?): Result<User> {
        val db = writableDatabase
        return try {
            val cv = ContentValues().apply {
                put(COL_USER_NOMBRE, nombre.trim())
                put(COL_USER_EMAIL, email.trim().lowercase())
                if (!newPassword.isNullOrBlank()) {
                    put(COL_USER_PASSWORD, newPassword)
                }
            }
            val rows = db.update(TABLE_USERS, cv, "$COL_USER_ID = ?", arrayOf(userId.toString()))
            if (rows > 0) {
                val updated = getUserById(userId)
                if (updated != null) {
                    addActivityLog(
                        hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                        titulo = "Perfil actualizado",
                        detalle = "Datos actualizados para el usuario ${updated.usuario}.",
                        tipo = "USUARIO"
                    )
                    Result.success(updated)
                } else {
                    Result.failure(Exception("Error al recargar usuario"))
                }
            } else {
                Result.failure(Exception("No se encontró el registro de usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserById(userId: Long): User? {
        val db = readableDatabase
        val cursor = db.query(TABLE_USERS, null, "$COL_USER_ID = ?", arrayOf(userId.toString()), null, null, null)
        return cursor.use {
            if (it.moveToFirst()) {
                User(
                    id = it.getLong(it.getColumnIndexOrThrow(COL_USER_ID)),
                    email = it.getString(it.getColumnIndexOrThrow(COL_USER_EMAIL)),
                    rut = it.getString(it.getColumnIndexOrThrow(COL_USER_RUT)),
                    nombreApellido = it.getString(it.getColumnIndexOrThrow(COL_USER_NOMBRE)),
                    usuario = it.getString(it.getColumnIndexOrThrow(COL_USER_USUARIO)),
                    contrasena = it.getString(it.getColumnIndexOrThrow(COL_USER_PASSWORD)),
                    fechaRegistro = it.getString(it.getColumnIndexOrThrow(COL_USER_FECHA))
                )
            } else null
        }
    }

    fun getAllDoors(plantaCod: String? = null): List<DoorData> {
        val db = readableDatabase
        val doors = mutableListOf<DoorData>()
        val selection = if (plantaCod != null && plantaCod != "TODAS") "$COL_DOOR_PLANTA_COD = ?" else null
        val selectionArgs = if (plantaCod != null && plantaCod != "TODAS") arrayOf(plantaCod) else null

        val cursor = db.query(TABLE_DOORS, null, selection, selectionArgs, null, null, "$COL_DOOR_ID ASC")
        cursor.use {
            while (it.moveToNext()) {
                val estadoStr = it.getString(it.getColumnIndexOrThrow(COL_DOOR_ESTADO))
                val estado = when (estadoStr.lowercase()) {
                    "abierta" -> DoorStatus.ABIERTA
                    "sin datos" -> DoorStatus.SIN_DATOS
                    else -> DoorStatus.CERRADA
                }
                doors.add(
                    DoorData(
                        id = it.getString(it.getColumnIndexOrThrow(COL_DOOR_ID)),
                        numeroPlano = it.getString(it.getColumnIndexOrThrow(COL_DOOR_NUM_PLANO)),
                        ubicacion = it.getString(it.getColumnIndexOrThrow(COL_DOOR_UBICACION)),
                        planta = it.getString(it.getColumnIndexOrThrow(COL_DOOR_PLANTA)),
                        estado = estado,
                        conectada = it.getInt(it.getColumnIndexOrThrow(COL_DOOR_CONECTADA)) == 1,
                        detalleConexion = it.getString(it.getColumnIndexOrThrow(COL_DOOR_DETALLE_CONEXION)),
                        alerta = it.getString(it.getColumnIndexOrThrow(COL_DOOR_ALERTA))
                    )
                )
            }
        }
        return doors
    }

    fun getDoorById(doorId: String): DoorData? {
        val db = readableDatabase
        val cursor = db.query(TABLE_DOORS, null, "$COL_DOOR_ID = ?", arrayOf(doorId), null, null, null)
        return cursor.use {
            if (it.moveToFirst()) {
                val estadoStr = it.getString(it.getColumnIndexOrThrow(COL_DOOR_ESTADO))
                val estado = when (estadoStr.lowercase()) {
                    "abierta" -> DoorStatus.ABIERTA
                    "sin datos" -> DoorStatus.SIN_DATOS
                    else -> DoorStatus.CERRADA
                }
                DoorData(
                    id = it.getString(it.getColumnIndexOrThrow(COL_DOOR_ID)),
                    numeroPlano = it.getString(it.getColumnIndexOrThrow(COL_DOOR_NUM_PLANO)),
                    ubicacion = it.getString(it.getColumnIndexOrThrow(COL_DOOR_UBICACION)),
                    planta = it.getString(it.getColumnIndexOrThrow(COL_DOOR_PLANTA)),
                    estado = estado,
                    conectada = it.getInt(it.getColumnIndexOrThrow(COL_DOOR_CONECTADA)) == 1,
                    detalleConexion = it.getString(it.getColumnIndexOrThrow(COL_DOOR_DETALLE_CONEXION)),
                    alerta = it.getString(it.getColumnIndexOrThrow(COL_DOOR_ALERTA))
                )
            } else null
        }
    }

    fun updateDoorStatus(doorId: String, newStatus: DoorStatus, alerta: String = "Sin alertas"): Boolean {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_DOOR_ESTADO, newStatus.label)
            put(COL_DOOR_ALERTA, alerta)
        }
        val rows = db.update(TABLE_DOORS, cv, "$COL_DOOR_ID = ?", arrayOf(doorId))
        if (rows > 0) {
            val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            addActivityLog(
                hora = hora,
                titulo = "Estado de puerta $doorId actualizado",
                detalle = "La puerta cambió a estado '${newStatus.label}'.",
                tipo = if (newStatus == DoorStatus.ABIERTA) "ALERTA" else "CIERRE"
            )
            return true
        }
        return false
    }

    fun getBuildingSummary(): BuildingSummary {
        val allDoors = getAllDoors()
        val total = allDoors.size
        val cerradas = allDoors.count { it.estado == DoorStatus.CERRADA }
        val abiertas = allDoors.count { it.estado == DoorStatus.ABIERTA }
        val sinDatos = allDoors.count { it.estado == DoorStatus.SIN_DATOS }
        val conectadas = allDoors.count { it.conectada }
        val desconectadas = total - conectadas

        // Group by floors
        val floorSummaries = listOf("PB", "P1", "P2", "P3").map { code ->
            val name = when (code) {
                "PB" -> "Planta baja"
                "P1" -> "Planta 1"
                "P2" -> "Planta 2"
                else -> "Planta 3"
            }
            val floorDoors = allDoors.filter { it.id.startsWith(code) }
            val alertsInFloor = floorDoors.count { it.estado == DoorStatus.ABIERTA || it.estado == DoorStatus.SIN_DATOS }
            val cerradasInFloor = floorDoors.count { it.estado == DoorStatus.CERRADA }
            FloorSummary(
                codigo = code,
                nombre = name,
                detalle = if (alertsInFloor > 0) "$alertsInFloor alertas" else "$cerradasInFloor cerradas",
                tieneAlerta = alertsInFloor > 0
            )
        }

        val lastActivities = getActivityLogs()
        val lastAct = lastActivities.firstOrNull() ?: ActivityItem("09:40", "Sin actividad reciente", "Supervisión activa.")

        val hasActiveIncident = abiertas > 0

        return BuildingSummary(
            nombreEdificio = "Edificio Alameda",
            fecha = "27 oct 2026",
            horaActualizacion = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            tieneIncidenteActivo = hasActiveIncident,
            detalleIncidente = if (hasActiveIncident) "Planta 2 · desde las 09:38" else "Sin incidentes activos",
            totalPuertas = total,
            cerradas = cerradas,
            abiertas = abiertas,
            sinDatos = sinDatos,
            conectadas = conectadas,
            desconectadas = desconectadas,
            plantas = floorSummaries,
            ultimaActividad = lastAct
        )
    }

    fun addActivityLog(hora: String, titulo: String, detalle: String, tipo: String) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_ACT_HORA, hora)
            put(COL_ACT_TITULO, titulo)
            put(COL_ACT_DETALLE, detalle)
            put(COL_ACT_TIPO, tipo)
        }
        db.insert(TABLE_ACTIVITY, null, cv)
    }

    fun getActivityLogs(): List<ActivityItem> {
        val db = readableDatabase
        val list = mutableListOf<ActivityItem>()
        val cursor = db.query(TABLE_ACTIVITY, null, null, null, null, null, "$COL_ACT_ID DESC")
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    ActivityItem(
                        hora = it.getString(it.getColumnIndexOrThrow(COL_ACT_HORA)),
                        titulo = it.getString(it.getColumnIndexOrThrow(COL_ACT_TITULO)),
                        detalle = it.getString(it.getColumnIndexOrThrow(COL_ACT_DETALLE))
                    )
                )
            }
        }
        return list
    }

    fun getFireSensorData(): FireSensorData {
        val db = readableDatabase
        val cursor = db.query(TABLE_FIRE, null, "$COL_FIRE_ID = 1", null, null, null, null)
        return cursor.use {
            if (it.moveToFirst()) {
                val nivelStr = it.getString(it.getColumnIndexOrThrow(COL_FIRE_NIVEL))
                val nivel = when (nivelStr.uppercase()) {
                    "BAJO" -> FireLevel.BAJO
                    "MEDIO" -> FireLevel.MEDIO
                    else -> FireLevel.ALTO
                }
                FireSensorData(
                    estado = it.getString(it.getColumnIndexOrThrow(COL_FIRE_ESTADO)),
                    valorAnalogo = it.getInt(it.getColumnIndexOrThrow(COL_FIRE_VALOR)),
                    timestamp = it.getString(it.getColumnIndexOrThrow(COL_FIRE_TIMESTAMP)),
                    nivel = nivel,
                    descripcionIntensidad = it.getString(it.getColumnIndexOrThrow(COL_FIRE_DESC)),
                    edificio = it.getString(it.getColumnIndexOrThrow(COL_FIRE_EDIFICIO)),
                    planta = it.getString(it.getColumnIndexOrThrow(COL_FIRE_PLANTA)),
                    esSimulado = true
                )
            } else {
                FireSensorData()
            }
        }
    }

    fun updateFireSensorData(newData: FireSensorData) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_FIRE_ESTADO, newData.estado)
            put(COL_FIRE_VALOR, newData.valorAnalogo)
            put(COL_FIRE_TIMESTAMP, newData.timestamp)
            put(COL_FIRE_NIVEL, newData.nivel.name)
            put(COL_FIRE_DESC, newData.descripcionIntensidad)
            put(COL_FIRE_EDIFICIO, newData.edificio)
            put(COL_FIRE_PLANTA, newData.planta)
        }
        db.update(TABLE_FIRE, cv, "$COL_FIRE_ID = 1", null)
    }
}
