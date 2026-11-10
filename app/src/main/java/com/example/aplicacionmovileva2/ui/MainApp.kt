package com.example.aplicacionmovileva2.ui

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.aplicacionmovileva2.ui.components.PrototipoBottomNav
import com.example.aplicacionmovileva2.ui.navigation.Screen
import com.example.aplicacionmovileva2.ui.screens.ActivityHistoryScreen
import com.example.aplicacionmovileva2.ui.screens.CredentialScreen
import com.example.aplicacionmovileva2.ui.screens.DoorDetailScreen
import com.example.aplicacionmovileva2.ui.screens.DoorsScreen
import com.example.aplicacionmovileva2.ui.screens.FireScreen
import com.example.aplicacionmovileva2.ui.screens.FloorPlanScreen
import com.example.aplicacionmovileva2.ui.screens.LoginScreen
import com.example.aplicacionmovileva2.ui.screens.ProfileScreen
import com.example.aplicacionmovileva2.ui.screens.RecoverPasswordScreen
import com.example.aplicacionmovileva2.ui.screens.RegisterScreen
import com.example.aplicacionmovileva2.ui.screens.SummaryScreen
import com.example.aplicacionmovileva2.ui.theme.BackgroundLight
import com.example.aplicacionmovileva2.ui.theme.DarkSlate
import com.example.aplicacionmovileva2.ui.theme.TextPrimary
import com.example.aplicacionmovileva2.ui.theme.TextSecondary
import com.example.aplicacionmovileva2.ui.viewmodel.FireSafetyViewModel

@Composable
fun MainApp() {
    val context = LocalContext.current
    val viewModel: FireSafetyViewModel = remember {
        FireSafetyViewModel(context.applicationContext as Application)
    }
    val uiState by viewModel.uiState.collectAsState()

    // Show bottom navigation on main app tabs only
    val showBottomNav = uiState.currentScreen !in listOf(
        Screen.LOGIN,
        Screen.REGISTER,
        Screen.RECOVER_PASSWORD,
        Screen.PROFILE,
        Screen.DOOR_DETAIL,
        Screen.CREDENCIAL_APERTURA,
        Screen.ACTIVITY_HISTORY
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        containerColor = BackgroundLight,
        bottomBar = {
            if (showBottomNav) {
                PrototipoBottomNav(
                    currentScreen = uiState.currentScreen,
                    onTabSelected = { selectedScreen ->
                        viewModel.navigateTo(selectedScreen)
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentScreen) {

                Screen.LOGIN -> {
                    LoginScreen(
                        usuarioOrRut = uiState.loginUserOrRut,
                        onUsuarioOrRutChange = { viewModel.onLoginUserOrRutChange(it) },
                        contrasena = uiState.loginPassword,
                        onContrasenaChange = { viewModel.onLoginPasswordChange(it) },
                        errorMessage = uiState.loginErrorMessage,
                        successMessage = uiState.loginSuccessNotification,
                        onLoginClick = { viewModel.login() },
                        onNavigateToRecover = { viewModel.navigateTo(Screen.RECOVER_PASSWORD) },
                        onNavigateToRegister = { viewModel.navigateTo(Screen.REGISTER) }
                    )
                }

                Screen.REGISTER -> {
                    RegisterScreen(
                        email = uiState.registerEmail,
                        onEmailChange = { viewModel.onRegisterEmailChange(it) },
                        rut = uiState.registerRut,
                        onRutChange = { viewModel.onRegisterRutChange(it) },
                        nombreApellido = uiState.registerNombreApellido,
                        onNombreApellidoChange = { viewModel.onRegisterNombreChange(it) },
                        usuario = uiState.registerUsuario,
                        onUsuarioChange = { viewModel.onRegisterUsuarioChange(it) },
                        contrasena = uiState.registerPassword,
                        onContrasenaChange = { viewModel.onRegisterPasswordChange(it) },
                        isLoading = uiState.registerLoading,
                        errorMessage = uiState.registerErrorMessage,
                        onCreateAccountClick = { viewModel.register() },
                        onNavigateToLogin = { viewModel.navigateTo(Screen.LOGIN) }
                    )
                }

                Screen.RECOVER_PASSWORD -> {
                    RecoverPasswordScreen(
                        identifier = uiState.recoverIdentifier,
                        onIdentifierChange = { viewModel.onRecoverIdentifierChange(it) },
                        newPassword = uiState.recoverNewPassword,
                        onNewPasswordChange = { viewModel.onRecoverNewPasswordChange(it) },
                        errorMessage = uiState.recoverErrorMessage,
                        successMessage = uiState.recoverSuccessMessage,
                        onResetPasswordClick = { viewModel.resetPassword() },
                        onNavigateBack = { viewModel.navigateTo(Screen.LOGIN) }
                    )
                }

                Screen.RESUMEN -> {
                    SummaryScreen(
                        buildingSummary = uiState.buildingSummary,
                        onNavigateToFire = { viewModel.navigateTo(Screen.INCENDIO) },
                        onNavigateToFloorPlan = { floorIndex ->
                            viewModel.selectFloor(floorIndex)
                            viewModel.navigateTo(Screen.PLANO)
                        },
                        onNavigateToHistory = { viewModel.navigateTo(Screen.ACTIVITY_HISTORY) },
                        userInitials = uiState.currentUser?.nombreApellido
                            ?.split(" ")?.take(2)
                            ?.mapNotNull { it.firstOrNull()?.uppercaseChar() }
                            ?.joinToString(""),
                        onNavigateToProfile = { viewModel.openProfile() }
                    )
                }

                Screen.PLANO -> {
                    FloorPlanScreen(
                        selectedFloorIndex = uiState.selectedFloorIndex,
                        onFloorSelected = { viewModel.selectFloor(it) },
                        selectedDoorId = uiState.selectedDoorIdOnPlan,
                        onDoorSelected = { viewModel.selectDoorOnPlan(it) },
                        doors = uiState.doors,
                        onNavigateToDoorDetail = { door ->
                            viewModel.openDoorDetail(door)
                        }
                    )
                }

                Screen.PUERTAS -> {
                    DoorsScreen(
                        doors = uiState.filteredDoors,
                        searchQuery = uiState.doorSearchQuery,
                        onSearchQueryChange = { viewModel.updateDoorSearchQuery(it) },
                        floorFilter = uiState.selectedDoorFloorFilter,
                        onFloorFilterChange = { viewModel.setDoorFloorFilter(it) },
                        statusFilter = uiState.selectedDoorStatusFilter,
                        onStatusFilterChange = { viewModel.setDoorStatusFilter(it) },
                        onDoorClick = { door ->
                            viewModel.openDoorDetail(door)
                        }
                    )
                }

                Screen.DOOR_DETAIL -> {
                    DoorDetailScreen(
                        door = uiState.selectedDoorForDetail,
                        onNavigateBack = { viewModel.navigateBack() },
                        onNavigateToCredential = { doorId ->
                            viewModel.selectDoorOnPlan(doorId)
                            viewModel.navigateTo(Screen.CREDENCIAL_APERTURA)
                        },
                        onToggleDoorState = { door ->
                            viewModel.toggleDoorStateFromDetail(door)
                        }
                    )
                }

                Screen.INCENDIO -> {
                    FireScreen(
                        sensorData = uiState.fireSensor,
                        onNavigateToCredential = { viewModel.navigateTo(Screen.CREDENCIAL_APERTURA) },
                        onCallEmergencyClick = { viewModel.showEmergencyDialog(true) }
                    )
                }

                Screen.CREDENCIAL_APERTURA -> {
                    CredentialScreen(
                        usuarioOrRut = uiState.credentialRutOrUser,
                        onUsuarioOrRutChange = { viewModel.onCredentialRutOrUserChange(it) },
                        contrasena = uiState.credentialPassword,
                        onContrasenaChange = { viewModel.onCredentialPasswordChange(it) },
                        isLoading = uiState.credentialLoading,
                        errorMessage = uiState.credentialErrorMessage,
                        onValidateClick = { viewModel.validateCredentialAndUnlock(uiState.selectedDoorIdOnPlan) },
                        onCancelClick = { viewModel.navigateBack() }
                    )
                }

                Screen.ACTIVITY_HISTORY -> {
                    ActivityHistoryScreen(
                        activities = uiState.activityLogs,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                }

                Screen.PROFILE -> {
                    ProfileScreen(
                        currentUser = uiState.currentUser,
                        profileNombre = uiState.profileNombre,
                        profileEmail = uiState.profileEmail,
                        profileNewPassword = uiState.profileNewPassword,
                        profileEditMode = uiState.profileEditMode,
                        profileLoading = uiState.profileLoading,
                        profileErrorMessage = uiState.profileErrorMessage,
                        profileSuccessMessage = uiState.profileSuccessMessage,
                        onNombreChange = { viewModel.onProfileNombreChange(it) },
                        onEmailChange = { viewModel.onProfileEmailChange(it) },
                        onNewPasswordChange = { viewModel.onProfileNewPasswordChange(it) },
                        onToggleEditMode = { viewModel.toggleProfileEditMode() },
                        onSaveClick = { viewModel.saveProfileChanges() },
                        onNavigateBack = { viewModel.navigateBack() },
                        onLogoutClick = { viewModel.logout() }
                    )
                }
            }
        }
    }

    // Emergency call dialog
    if (uiState.emergencyCallDialogVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.showEmergencyDialog(false) },
            title = {
                Text(
                    text = "Llamar a emergencias",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = "Confirmación para iniciar enlace directo con la central de emergencias.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.showEmergencyDialog(false) }) {
                    Text(text = "Entendido", color = DarkSlate, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // General message / feedback dialog
    if (uiState.messageDialogText != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissMessageDialog() },
            title = {
                Text(
                    text = "Validación de credencial",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = uiState.messageDialogText ?: "",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.dismissMessageDialog()
                    viewModel.navigateTo(Screen.INCENDIO)
                }) {
                    Text(text = "Entendido", color = DarkSlate, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
