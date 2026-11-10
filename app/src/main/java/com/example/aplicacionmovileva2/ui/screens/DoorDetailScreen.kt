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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.DoorBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicacionmovileva2.data.model.DoorData
import com.example.aplicacionmovileva2.data.model.DoorStatus
import com.example.aplicacionmovileva2.ui.components.OutlineActionButton
import com.example.aplicacionmovileva2.ui.components.PrimaryActionButton
import com.example.aplicacionmovileva2.ui.components.PrototipoFooter
import com.example.aplicacionmovileva2.ui.components.PrototipoHeader
import com.example.aplicacionmovileva2.ui.components.StatusBadge
import com.example.aplicacionmovileva2.ui.theme.AlertRedText
import com.example.aplicacionmovileva2.ui.theme.BackgroundLight
import com.example.aplicacionmovileva2.ui.theme.BorderLight
import com.example.aplicacionmovileva2.ui.theme.BorderSubtle
import com.example.aplicacionmovileva2.ui.theme.DarkSlate
import com.example.aplicacionmovileva2.ui.theme.SurfaceWhite
import com.example.aplicacionmovileva2.ui.theme.TextPrimary
import com.example.aplicacionmovileva2.ui.theme.TextSecondary
import com.example.aplicacionmovileva2.ui.theme.WarningAmber

@Composable
fun DoorDetailScreen(
    door: DoorData?,
    onNavigateBack: () -> Unit,
    onNavigateToCredential: (String) -> Unit,
    onToggleDoorState: (DoorData) -> Unit,
    modifier: Modifier = Modifier
) {
    if (door == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Puerta no encontrada", fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryActionButton(text = "Volver", onClick = onNavigateBack)
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
    ) {
        PrototipoHeader()

        Spacer(modifier = Modifier.height(8.dp))

        // Title & subtitle
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Detalle de puerta",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${door.id} · ${door.ubicacion} (${door.planta})",
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
                text = "Volver",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkSlate
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Main Door Status Card
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
                    Column {
                        Text(
                            text = door.id,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = door.ubicacion,
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                    StatusBadge(status = door.estado)
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(thickness = 1.dp, color = BorderSubtle)
                Spacer(modifier = Modifier.height(14.dp))

                DetailRow(label = "Planta", value = door.planta)
                DetailRow(label = "Número de acceso", value = "Acceso ${door.numeroPlano}")
                DetailRow(
                    label = "Diagnóstico / Alerta",
                    value = door.alerta,
                    valueColor = when (door.estado) {
                        DoorStatus.ABIERTA -> AlertRedText
                        DoorStatus.SIN_DATOS -> WarningAmber
                        DoorStatus.CERRADA -> TextPrimary
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Door Information Card
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
                        imageVector = Icons.Default.DoorBack,
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Información de la puerta",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (door.conectada) Icons.Default.Wifi else Icons.Default.WifiOff,
                            contentDescription = null,
                            tint = if (door.conectada) TextSecondary else WarningAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Estado de conexión",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                    Text(
                        text = door.detalleConexion,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (door.conectada) TextPrimary else WarningAmber
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                DetailRow(label = "Estado del sensor", value = if (door.estado == DoorStatus.ABIERTA) "Puerta Abierta" else "Puerta Cerrada")
                DetailRow(label = "Última actualización", value = "Hoy a las 09:41 hrs")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Interactive Action Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            if (door.estado == DoorStatus.CERRADA) {
                PrimaryActionButton(
                    text = "Solicitar apertura con credencial",
                    icon = Icons.Default.Lock,
                    onClick = { onNavigateToCredential(door.id) }
                )
            } else {
                PrimaryActionButton(
                    text = "Confirmar cierre de puerta",
                    icon = Icons.Default.DoorBack,
                    onClick = { onToggleDoorState(door) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlineActionButton(
                text = "Cambiar estado manualmente",
                onClick = { onToggleDoorState(door) }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        PrototipoFooter()

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = TextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}
