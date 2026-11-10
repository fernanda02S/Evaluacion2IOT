package com.example.aplicacionmovileva2.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos recibido desde el sistema IoT (Arduino ESP8266/ESP32).
 * Compatible con la estructura JSON de telemetría de incendio.
 */
data class FireSensorData(
    @SerializedName("estado")
    val estado: String = "Alerta", // "Normal" o "Alerta"

    @SerializedName("valor_analogo")
    val valorAnalogo: Int = 890, // Valor analógico del sensor de flama (0 - 1023)

    @SerializedName("timestamp")
    val timestamp: String = "09:41", // Marca de tiempo del reporte

    @SerializedName("nivel")
    val nivel: FireLevel = FireLevel.ALTO,

    @SerializedName("descripcion_intensidad")
    val descripcionIntensidad: String = "Intensidad de llama alta",

    @SerializedName("edificio")
    val edificio: String = "Edificio Alameda",

    @SerializedName("planta")
    val planta: String = "Planta 2",

    @SerializedName("es_simulado")
    val esSimulado: Boolean = true
)

enum class FireLevel(val label: String, val displayDetail: String) {
    BAJO("Bajo", "Intensidad de llama baja"),
    MEDIO("Medio", "Intensidad de llama moderada"),
    ALTO("Alto", "Intensidad de llama alta")
}

/**
 * Petición para validación de credencial de apertura.
 */
data class CredentialAuthRequest(
    @SerializedName("usuario_o_rut")
    val usuarioORut: String,

    @SerializedName("contrasena")
    val contrasena: String,

    @SerializedName("puerta_id")
    val puertaId: String = "P2-04"
)

/**
 * Respuesta de validación de credencial de apertura.
 */
data class CredentialAuthResponse(
    @SerializedName("autorizado")
    val autorizado: Boolean,

    @SerializedName("mensaje")
    val mensaje: String,

    @SerializedName("token")
    val token: String? = null
)
