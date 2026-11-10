package com.example.aplicacionmovileva2.data.model

import com.google.gson.annotations.SerializedName

enum class DoorStatus(val label: String) {
    CERRADA("Cerrada"),
    ABIERTA("Abierta"),
    SIN_DATOS("Sin datos")
}

data class DoorData(
    @SerializedName("id")
    val id: String, // ej. "P2-04"

    @SerializedName("numero_plano")
    val numeroPlano: String, // ej. "04"

    @SerializedName("ubicacion")
    val ubicacion: String, // ej. "Escalera A"

    @SerializedName("planta")
    val planta: String, // ej. "Planta 2"

    @SerializedName("estado")
    val estado: DoorStatus,

    @SerializedName("conectada")
    val conectada: Boolean = true,

    @SerializedName("detalle_conexion")
    val detalleConexion: String = "Conectada", // "Conectada" o "Sin conexión - 09:37"

    @SerializedName("alerta")
    val alerta: String = "Sin alertas" // "Alerta de cierre", "Sin telemetría", "Sin alertas"
)
