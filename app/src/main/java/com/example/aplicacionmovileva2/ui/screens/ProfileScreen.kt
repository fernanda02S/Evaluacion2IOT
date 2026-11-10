package com.example.aplicacionmovileva2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicacionmovileva2.data.model.User
import com.example.aplicacionmovileva2.ui.components.AppPasswordField
import com.example.aplicacionmovileva2.ui.components.AppTextField
import com.example.aplicacionmovileva2.ui.components.PrototipoFooter
import com.example.aplicacionmovileva2.ui.components.PrototipoHeader
import com.example.aplicacionmovileva2.ui.theme.AlertRed
import com.example.aplicacionmovileva2.ui.theme.AlertRedBg
import com.example.aplicacionmovileva2.ui.theme.AlertRedBorder
import com.example.aplicacionmovileva2.ui.theme.BackgroundLight
import com.example.aplicacionmovileva2.ui.theme.BorderLight
import com.example.aplicacionmovileva2.ui.theme.BorderSubtle
import com.example.aplicacionmovileva2.ui.theme.DarkSlate
import com.example.aplicacionmovileva2.ui.theme.SuccessGreen
import com.example.aplicacionmovileva2.ui.theme.SuccessGreenBg
import com.example.aplicacionmovileva2.ui.theme.SuccessGreenBorder
import com.example.aplicacionmovileva2.ui.theme.SurfaceWhite
import com.example.aplicacionmovileva2.ui.theme.TextMuted
import com.example.aplicacionmovileva2.ui.theme.TextPrimary
import com.example.aplicacionmovileva2.ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    currentUser: User?,
    profileNombre: String,
    profileEmail: String,
    profileNewPassword: String,
    profileEditMode: Boolean,
    profileLoading: Boolean,
    profileErrorMessage: String?,
    profileSuccessMessage: String?,
    onNombreChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onToggleEditMode: () -> Unit,
    onSaveClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user = currentUser

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
    ) {
        PrototipoHeader()

        Spacer(modifier = Modifier.height(8.dp))

        // Back button + Title
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onNavigateBack() }
                    .padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Volver",
                    tint = DarkSlate,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "Volver al panel",
                    fontSize = 13.sp,
                    color = DarkSlate,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Mi perfil",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Gestiona tus datos personales y seguridad.",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- Avatar + Identity Card ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceWhite)
                .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Avatar circle with initials
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(DarkSlate),
                        contentAlignment = Alignment.Center
                    ) {
                        val initials = (user?.nombreApellido ?: "?")
                            .split(" ")
                            .take(2)
                            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                            .joinToString("")
                            .ifBlank { "?" }
                        Text(
                            text = initials,
                            color = androidx.compose.ui.graphics.Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user?.nombreApellido ?: "Usuario",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "@${user?.usuario ?: "usuario"}",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(thickness = 1.dp, color = BorderSubtle)
                Spacer(modifier = Modifier.height(14.dp))

                // Identity fields (read-only)
                ProfileReadOnlyRow(label = "RUT", value = user?.rut ?: "-")
                Spacer(modifier = Modifier.height(10.dp))
                ProfileReadOnlyRow(label = "Usuario", value = user?.usuario ?: "-")
                Spacer(modifier = Modifier.height(10.dp))
                ProfileReadOnlyRow(label = "Fecha de registro", value = user?.fechaRegistro ?: "-")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Editable Data Card ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceWhite)
                .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Datos personales",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (!profileLoading) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onToggleEditMode() }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (profileEditMode) Icons.Default.Lock else Icons.Default.Edit,
                                contentDescription = if (profileEditMode) "Cancelar edición" else "Editar",
                                tint = DarkSlate,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (profileEditMode) "Cancelar" else "Editar",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkSlate
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name field
                Text(
                    text = "Nombre y apellido",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                if (profileEditMode) {
                    AppTextField(
                        value = profileNombre,
                        onValueChange = onNombreChange,
                        placeholder = "Tu nombre y apellido"
                    )
                } else {
                    ProfileValueBox(value = profileNombre.ifBlank { user?.nombreApellido ?: "-" })
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Email field
                Text(
                    text = "Correo electrónico",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                if (profileEditMode) {
                    AppTextField(
                        value = profileEmail,
                        onValueChange = onEmailChange,
                        placeholder = "nombre@correo.cl",
                        keyboardType = KeyboardType.Email
                    )
                } else {
                    ProfileValueBox(value = profileEmail.ifBlank { user?.email ?: "-" })
                }

                // Password change — only in edit mode
                if (profileEditMode) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Nueva contraseña",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Dejar vacío para mantener la contraseña actual.",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    AppPasswordField(
                        value = profileNewPassword,
                        onValueChange = onNewPasswordChange,
                        placeholder = "Mínimo 8 caracteres"
                    )
                }

                // Error / Success messages
                if (profileErrorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(AlertRedBg)
                            .border(1.dp, AlertRedBorder, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = profileErrorMessage,
                            fontSize = 13.sp,
                            color = AlertRed
                        )
                    }
                }

                if (profileSuccessMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SuccessGreenBg)
                            .border(1.dp, SuccessGreenBorder, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = profileSuccessMessage,
                            fontSize = 13.sp,
                            color = SuccessGreen
                        )
                    }
                }

                // Save button — only in edit mode
                if (profileEditMode) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onSaveClick,
                        enabled = !profileLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkSlate)
                    ) {
                        if (profileLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = androidx.compose.ui.graphics.Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null,
                                tint = androidx.compose.ui.graphics.Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Guardar cambios",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = androidx.compose.ui.graphics.Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Logout Button ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceWhite)
                .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Sesión",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Al cerrar sesión tendrás que volver a ingresar tus credenciales.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedButton(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, AlertRed),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AlertRed)
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Cerrar sesión",
                        modifier = Modifier.size(18.dp),
                        tint = AlertRed
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cerrar sesión",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AlertRed
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        PrototipoFooter()
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileReadOnlyRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = TextSecondary
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary
        )
    }
}

@Composable
private fun ProfileValueBox(value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(BackgroundLight)
            .border(1.dp, BorderLight, RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(
            text = value,
            fontSize = 14.sp,
            color = TextPrimary
        )
    }
}
