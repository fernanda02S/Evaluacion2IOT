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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicacionmovileva2.ui.components.AppPasswordField
import com.example.aplicacionmovileva2.ui.components.AppTextField
import com.example.aplicacionmovileva2.ui.components.PrimaryActionButton
import com.example.aplicacionmovileva2.ui.components.PrototipoFooter
import com.example.aplicacionmovileva2.ui.components.PrototipoHeader
import com.example.aplicacionmovileva2.ui.theme.AlertRedText
import com.example.aplicacionmovileva2.ui.theme.BackgroundLight
import com.example.aplicacionmovileva2.ui.theme.BorderLight
import com.example.aplicacionmovileva2.ui.theme.DarkSlate
import com.example.aplicacionmovileva2.ui.theme.SurfaceWhite
import com.example.aplicacionmovileva2.ui.theme.TextMuted
import com.example.aplicacionmovileva2.ui.theme.TextPrimary
import com.example.aplicacionmovileva2.ui.theme.TextSecondary

@Composable
fun RegisterScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    rut: String,
    onRutChange: (String) -> Unit,
    nombreApellido: String,
    onNombreApellidoChange: (String) -> Unit,
    usuario: String,
    onUsuarioChange: (String) -> Unit,
    contrasena: String,
    onContrasenaChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onCreateAccountClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        PrototipoHeader()

        Spacer(modifier = Modifier.height(8.dp))

        // Main Title & Subtitle
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Registro de usuario",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Crea tu cuenta para acceder a la aplicación.",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Card "Datos de la cuenta"
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
                        text = "Datos de la cuenta",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Todos obligatorios",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Correo electrónico",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                AppTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    placeholder = "nombre@correo.cl",
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "RUT",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                AppTextField(
                    value = rut,
                    onValueChange = onRutChange,
                    placeholder = "12.345.678-9"
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Nombre y apellido",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                AppTextField(
                    value = nombreApellido,
                    onValueChange = onNombreApellidoChange,
                    placeholder = "Ingresa tu nombre y apellido"
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Usuario",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                AppTextField(
                    value = usuario,
                    onValueChange = onUsuarioChange,
                    placeholder = "Elige un nombre de usuario"
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Contraseña",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                AppPasswordField(
                    value = contrasena,
                    onValueChange = onContrasenaChange,
                    placeholder = "••••••••"
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Usa al menos 8 caracteres.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = errorMessage,
                        fontSize = 12.sp,
                        color = AlertRedText
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = DarkSlate,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    PrimaryActionButton(
                        text = "Crear cuenta",
                        onClick = onCreateAccountClick
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bottom link: "¿Ya tienes una cuenta? Ingresar"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿Ya tienes una cuenta? ",
                fontSize = 13.sp,
                color = TextSecondary
            )
            Text(
                text = "Ingresar",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        PrototipoFooter()

        Spacer(modifier = Modifier.height(24.dp))
    }
}
