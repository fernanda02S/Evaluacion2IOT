package com.example.aplicacionmovileva2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
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
import com.example.aplicacionmovileva2.ui.theme.SuccessGreen
import com.example.aplicacionmovileva2.ui.theme.SuccessGreenBg
import com.example.aplicacionmovileva2.ui.theme.SuccessGreenBorder
import com.example.aplicacionmovileva2.ui.theme.SurfaceWhite
import com.example.aplicacionmovileva2.ui.theme.TextPrimary
import com.example.aplicacionmovileva2.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    usuarioOrRut: String,
    onUsuarioOrRutChange: (String) -> Unit,
    contrasena: String,
    onContrasenaChange: (String) -> Unit,
    errorMessage: String?,
    successMessage: String?,
    onLoginClick: () -> Unit,
    onNavigateToRecover: () -> Unit,
    onNavigateToRegister: () -> Unit,
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
                text = "Ingreso de usuario",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ingresa tus datos para acceder a la aplicación.",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Success Banner if just registered or recovered
        if (successMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SuccessGreenBg)
                    .border(1.dp, SuccessGreenBorder, RoundedCornerShape(10.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = successMessage,
                    fontSize = 13.sp,
                    color = SuccessGreen,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 17.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Card "Acceso a tu cuenta"
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
                    text = "Acceso a tu cuenta",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Usuario o RUT",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                AppTextField(
                    value = usuarioOrRut,
                    onValueChange = onUsuarioOrRutChange,
                    placeholder = "Ingresa tu usuario o RUT"
                )

                Spacer(modifier = Modifier.height(16.dp))

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

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = errorMessage,
                        fontSize = 12.sp,
                        color = AlertRedText
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToRecover() },
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                PrimaryActionButton(
                    text = "Ingresar",
                    onClick = onLoginClick
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bottom link: "¿No tienes una cuenta? Crear cuenta"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                text = "¿No tienes una cuenta? ",
                fontSize = 13.sp,
                color = TextSecondary
            )
            Text(
                text = "Crear cuenta",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        PrototipoFooter()

        Spacer(modifier = Modifier.height(24.dp))
    }
}
