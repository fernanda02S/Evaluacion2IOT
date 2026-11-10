package com.example.aplicacionmovileva2.ui.viewmodel

import com.example.aplicacionmovileva2.data.model.ActivityItem
import com.example.aplicacionmovileva2.data.model.BuildingSummary
import com.example.aplicacionmovileva2.data.model.DoorData
import com.example.aplicacionmovileva2.data.model.FireSensorData
import com.example.aplicacionmovileva2.data.model.User
import com.example.aplicacionmovileva2.ui.navigation.Screen

data class FireSafetyUiState(
    val currentScreen: Screen = Screen.LOGIN,
    val previousScreen: Screen? = null,
    val fireSensor: FireSensorData = FireSensorData(),
    val buildingSummary: BuildingSummary = BuildingSummary(),
    val doors: List<DoorData> = emptyList(),
    val filteredDoors: List<DoorData> = emptyList(),
    val activityLogs: List<ActivityItem> = emptyList(),

    // Authenticated user session
    val currentUser: User? = null,

    // Login screen state
    val loginUserOrRut: String = "",
    val loginPassword: String = "",
    val loginErrorMessage: String? = null,
    val loginSuccessNotification: String? = null,

    // Register screen state
    val registerEmail: String = "",
    val registerRut: String = "",
    val registerNombreApellido: String = "",
    val registerUsuario: String = "",
    val registerPassword: String = "",
    val registerErrorMessage: String? = null,
    val registerLoading: Boolean = false,

    // Recover password state
    val recoverIdentifier: String = "",
    val recoverNewPassword: String = "",
    val recoverErrorMessage: String? = null,
    val recoverSuccessMessage: String? = null,

    // Profile screen state
    val profileNombre: String = "",
    val profileEmail: String = "",
    val profileNewPassword: String = "",
    val profileEditMode: Boolean = false,
    val profileLoading: Boolean = false,
    val profileErrorMessage: String? = null,
    val profileSuccessMessage: String? = null,

    // Plano screen state
    val selectedFloorIndex: Int = 2, // 0: Baja (PB), 1: Planta 1 (P1), 2: Planta 2 (P2), 3: Planta 3 (P3)
    val selectedDoorIdOnPlan: String = "P2-04",

    // Puertas screen search & filter state
    val doorSearchQuery: String = "",
    val selectedDoorFloorFilter: String = "Planta 2", // "Todas", "Planta baja", "Planta 1", "Planta 2", "Planta 3"
    val selectedDoorStatusFilter: String = "todos", // "todos", "abierta", "cerrada", "sin datos"

    // Door detail screen state
    val selectedDoorForDetail: DoorData? = null,

    // Credential screen form
    val credentialRutOrUser: String = "",
    val credentialPassword: String = "",
    val credentialLoading: Boolean = false,
    val credentialSuccessMessage: String? = null,
    val credentialErrorMessage: String? = null,

    // Dialogs / Feedback state
    val emergencyCallDialogVisible: Boolean = false,
    val messageDialogText: String? = null
)
