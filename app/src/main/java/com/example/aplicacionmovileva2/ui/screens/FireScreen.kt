package com.example.aplicacionmovileva2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicacionmovileva2.data.model.FireSensorData
import com.example.aplicacionmovileva2.ui.components.IconIncendioFlame
import com.example.aplicacionmovileva2.ui.components.OutlineActionButton
import com.example.aplicacionmovileva2.ui.components.PrototipoFooter
import com.example.aplicacionmovileva2.ui.components.PrototipoHeader
import com.example.aplicacionmovileva2.ui.theme.AlertRed
import com.example.aplicacionmovileva2.ui.theme.AlertRedBg
import com.example.aplicacionmovileva2.ui.theme.AlertRedText
import com.example.aplicacionmovileva2.ui.theme.BackgroundLight
import com.example.aplicacionmovileva2.ui.theme.BorderLight
import com.example.aplicacionmovileva2.ui.theme.BorderSubtle
import com.example.aplicacionmovileva2.ui.theme.SurfaceWhite
import com.example.aplicacionmovileva2.ui.theme.TextMuted
import com.example.aplicacionmovileva2.ui.theme.TextPrimary
import com.example.aplicacionmovileva2.ui.theme.TextSecondary

@Composable
fun FireScreen(
    sensorData: FireSensorData,
    onNavigateToCredential: () -> Unit,
    onCallEmergencyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
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
                text = "Alerta de Incendio",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${sensorData.edificio} · ${sensorData.planta}",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Fire Level Card
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
                // Header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nivel de incendio",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    // Badge: Estado actual · 09:41
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BorderSubtle)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Estado actual · ${sensorData.timestamp}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Flame icon + Level text row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Flame icon in soft rounded square
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AlertRedBg),
                        contentAlignment = Alignment.Center
                    ) {
                        IconIncendioFlame(
                            tint = AlertRed,
                            size = 30.dp,
                            filled = false
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = sensorData.nivel.label,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = AlertRedText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = sensorData.descripcionIntensidad,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // 3-segment progress indicator (Baja, Media, Alta - actual)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Segment 1: Baja (gray bar)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(BorderLight)
                    )
                    // Segment 2: Media (tan/amber bar)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFFD4B896))
                    )
                    // Segment 3: Alta - actual (vibrant red bar)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(AlertRed)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Labels below segments
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Baja",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Media",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Alta - actual",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AlertRedText,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Card Footnote
                Text(
                    text = "Evaluación de riesgo por nivel de alerta en planta.",
                    fontSize = 11.sp,
                    color = TextMuted,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Authorized Personnel Card ("Solo personal autorizado")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceWhite)
                .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
                .padding(18.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Gestión de acceso de emergencia",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlineActionButton(
                    text = "Abrir puerta",
                    onClick = { onNavigateToCredential() }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Requiere credencial de usuario administrador.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Prominent Emergency Call Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onCallEmergencyClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AlertRed,
                    contentColor = SurfaceWhite
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = SurfaceWhite,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Llamar a emergencias",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SurfaceWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        PrototipoFooter()

        Spacer(modifier = Modifier.height(24.dp))
    }
}
