package com.example.aplicacionmovileva2.ui.navigation

enum class Screen(val title: String) {
    LOGIN("Ingreso de usuario"),
    REGISTER("Registro de usuario"),
    RECOVER_PASSWORD("Recuperar contraseña"),
    RESUMEN("Resumen"),
    PLANO("Plano"),
    PUERTAS("Puertas"),
    DOOR_DETAIL("Detalle de puerta"),
    INCENDIO("Incendio"),
    CREDENCIAL_APERTURA("Credencial de apertura"),
    ACTIVITY_HISTORY("Historial de actividad"),
    PROFILE("Mi perfil")
}

enum class BottomTab(val route: Screen, val label: String) {
    RESUMEN(Screen.RESUMEN, "Resumen"),
    PLANO(Screen.PLANO, "Plano"),
    PUERTAS(Screen.PUERTAS, "Puertas"),
    INCENDIO(Screen.INCENDIO, "Incendio")
}
