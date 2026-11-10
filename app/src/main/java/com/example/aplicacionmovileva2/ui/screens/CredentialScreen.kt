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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.aplicacionmovileva2.ui.theme.BorderSubtle
import com.example.aplicacionmovileva2.ui.theme.DarkSlate
import com.example.aplicacionmovileva2.ui.theme.PlanRoomBg
import com.example.aplicacionmovileva2.ui.theme.SurfaceWhite
import com.example.aplicacionmovileva2.ui.theme.TextMuted
import com.example.aplicacionmovileva2.ui.theme.TextPrimary
import com.example.aplicacionmovileva2.ui.theme.TextSecondary

@Composable
fun CredentialScreen(
    usuarioOrRut: String,
    onUsuarioOrRutChange: (String) -> Unit,
    contrasena: String,
    onContrasenaChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onValidateClick: () -> Unit,
    onCancelClick: () -> Unit,
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
                text = "Credencial de apertura",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Verifica tu identidad antes de autorizar la puerta.",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back link: "< Volver"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .clickable { onCancelClick() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = DarkSlate,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Volver",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkSlate
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Card "Solo personal autorizado"
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
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Acceso de administrador",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Ingresa tu credencial para solicitar la autorización de apertura.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
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
                    placeholder = "Ingresa tu contraseña"
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
                        text = "Validar credencial",
                        onClick = onValidateClick
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "La orden será procesada y guardada en la base de datos.",
                    fontSize = 11.sp,
                    color = TextMuted,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Link "Cancelar solicitud"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCancelClick() }
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Cancelar solicitud",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Info Banner Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PlanRoomBg)
                .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Acción registrada en la base de datos de administración.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        PrototipoFooter()

        Spacer(modifier = Modifier.height(24.dp))
    }
}
