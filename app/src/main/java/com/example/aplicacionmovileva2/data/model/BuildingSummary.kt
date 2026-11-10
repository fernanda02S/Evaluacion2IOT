package com.example.aplicacionmovileva2.data.model

data class FloorSummary(
    val codigo: String, // "PB", "P1", "P2", "P3"
    val nombre: String, // "Planta baja", "Planta 1", "Planta 2", "Planta 3"
    val detalle: String, // "6 cerradas", "2 alertas"
    val tieneAlerta: Boolean = false
)

data class ActivityItem(
    val hora: String = "09:40",
    val titulo: String = "P2-04 sigue abierta",
    val detalle: String = "Verificación asignada a Laura Martin."
)

data class BuildingSummary(
    val nombreEdificio: String = "Edificio Alameda",
    val fecha: String = "27 oct 2026",
    val horaActualizacion: String = "09:41",
    val tieneIncidenteActivo: Boolean = true,
    val detalleIncidente: String = "Planta 2 · desde las 09:38",
    val totalPuertas: Int = 24,
    val cerradas: Int = 22,
    val abiertas: Int = 1,
    val sinDatos: Int = 1,
    val conectadas: Int = 23,
    val desconectadas: Int = 1,
    val plantas: List<FloorSummary> = listOf(
        FloorSummary("PB", "Planta baja", "6 cerradas", tieneAlerta = false),
        FloorSummary("P1", "Planta 1", "6 cerradas", tieneAlerta = false),
        FloorSummary("P2", "Planta 2", "2 alertas", tieneAlerta = true),
        FloorSummary("P3", "Planta 3", "6 cerradas", tieneAlerta = false)
    ),
    val ultimaActividad: ActivityItem = ActivityItem()
)
