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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material3.Icon
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
import com.example.aplicacionmovileva2.ui.theme.DarkSlate
import com.example.aplicacionmovileva2.ui.theme.SuccessGreen
import com.example.aplicacionmovileva2.ui.theme.SuccessGreenBg
import com.example.aplicacionmovileva2.ui.theme.SuccessGreenBorder
import com.example.aplicacionmovileva2.ui.theme.SurfaceWhite
import com.example.aplicacionmovileva2.ui.theme.TextPrimary
import com.example.aplicacionmovileva2.ui.theme.TextSecondary

@Composable
fun RecoverPasswordScreen(
    identifier: String,
    onIdentifierChange: (String) -> Unit,
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    errorMessage: String?,
    successMessage: String?,
    onResetPasswordClick: () -> Unit,
    onNavigateBack: () -> Unit,
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
                text = "Recuperar contraseña",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ingresa tu identificación para restablecer tu clave.",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back link
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .clickable { onNavigateBack() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = DarkSlate,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Volver al inicio de sesión",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkSlate
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (successMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SuccessGreenBg)
                    .border(1.dp, SuccessGreenBorder, RoundedCornerShape(10.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "$successMessage Ya puedes ingresar con tu nueva clave.",
                    fontSize = 13.sp,
                    color = SuccessGreen,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Card
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LockReset,
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Restablecimiento de clave",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Usuario, RUT o Correo electrónico",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                AppTextField(
                    value = identifier,
                    onValueChange = onIdentifierChange,
                    placeholder = "Ej: 12.345.678-9 o usuario"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Nueva contraseña",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                AppPasswordField(
                    value = newPassword,
                    onValueChange = onNewPasswordChange,
                    placeholder = "Al menos 8 caracteres"
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

                PrimaryActionButton(
                    text = "Actualizar contraseña",
                    onClick = onResetPasswordClick
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        PrototipoFooter()

        Spacer(modifier = Modifier.height(24.dp))
    }
}
