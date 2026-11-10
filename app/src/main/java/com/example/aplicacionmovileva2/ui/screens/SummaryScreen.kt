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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
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
import com.example.aplicacionmovileva2.data.model.BuildingSummary
import com.example.aplicacionmovileva2.data.model.FloorSummary
import com.example.aplicacionmovileva2.ui.components.PrototipoFooter
import com.example.aplicacionmovileva2.ui.components.PrototipoHeader
import com.example.aplicacionmovileva2.ui.theme.AlertRed
import com.example.aplicacionmovileva2.ui.theme.AlertRedBg
import com.example.aplicacionmovileva2.ui.theme.AlertRedBorder
import com.example.aplicacionmovileva2.ui.theme.AlertRedText
import com.example.aplicacionmovileva2.ui.theme.BackgroundLight
import com.example.aplicacionmovileva2.ui.theme.BorderLight
import com.example.aplicacionmovileva2.ui.theme.BorderSubtle
import com.example.aplicacionmovileva2.ui.theme.DarkSlate
import com.example.aplicacionmovileva2.ui.theme.PlanRoomBg
import com.example.aplicacionmovileva2.ui.theme.SuccessGreen
import com.example.aplicacionmovileva2.ui.theme.SurfaceWhite
import com.example.aplicacionmovileva2.ui.theme.TextMuted
import com.example.aplicacionmovileva2.ui.theme.TextPrimary
import com.example.aplicacionmovileva2.ui.theme.TextSecondary
import com.example.aplicacionmovileva2.ui.theme.WarningAmber

@Composable
fun SummaryScreen(
    buildingSummary: BuildingSummary,
    onNavigateToFire: () -> Unit,
    onNavigateToFloorPlan: (Int) -> Unit,
    onNavigateToHistory: () -> Unit,
    userInitials: String? = null,
    onNavigateToProfile: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
    ) {
        PrototipoHeader(
            userInitials = userInitials,
            onProfileClick = onNavigateToProfile
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Main Title & Subtitle
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Resumen",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${buildingSummary.nombreEdificio} · ${buildingSummary.fecha}",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Active Incident Banner Card
        if (buildingSummary.tieneIncidenteActivo) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AlertRedBg)
                    .border(1.dp, AlertRedBorder, RoundedCornerShape(12.dp))
                    .clickable { onNavigateToFire() }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AlertRed.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Alerta",
                            tint = AlertRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Incidente activo",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AlertRedText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = buildingSummary.detalleIncidente,
                            fontSize = 12.sp,
                            color = AlertRedText.copy(alpha = 0.85f)
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Ver incidente",
                        tint = AlertRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Card "24 puertas supervisadas"
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
                        text = "${buildingSummary.totalPuertas} puertas supervisadas",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = buildingSummary.horaActualizacion,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatItem(
                        count = "${buildingSummary.cerradas}",
                        label = "Cerradas",
                        color = SuccessGreen
                    )
                    StatItem(
                        count = "${buildingSummary.abiertas}",
                        label = "Abierta",
                        color = AlertRed
                    )
                    StatItem(
                        count = "${buildingSummary.sinDatos}",
                        label = "Sin datos",
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(thickness = 1.dp, color = BorderSubtle)
                Spacer(modifier = Modifier.height(14.dp))

                // Connectivity status
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${buildingSummary.conectadas}/${buildingSummary.totalPuertas} conectadas · ",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "${buildingSummary.desconectadas} sin conexión",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = WarningAmber
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section "Estado por planta"
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estado por planta",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "4 plantas · 6 puertas/planta",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Floors Card List
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceWhite)
                    .border(1.dp, BorderLight, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    buildingSummary.plantas.forEachIndexed { index, floor ->
                        FloorRowItem(
                            floor = floor,
                            onClick = {
                                val floorIdx = when (floor.codigo) {
                                    "PB" -> 0
                                    "P1" -> 1
                                    "P2" -> 2
                                    "P3" -> 3
                                    else -> 2
                                }
                                onNavigateToFloorPlan(floorIdx)
                            }
                        )
                        if (index < buildingSummary.plantas.size - 1) {
                            HorizontalDivider(thickness = 1.dp, color = BorderSubtle)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section "Última actividad"
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Última actividad",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Ver historial",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkSlate,
                    modifier = Modifier.clickable { onNavigateToHistory() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Timeline item (clickable to see history)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToHistory() }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = buildingSummary.ultimaActividad.hora,
                    fontSize = 12.sp,
                    color = TextMuted,
                    modifier = Modifier.width(44.dp)
                )

                // Vertical red bar
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(38.dp)
                        .clip(RoundedCornerShape(1.5.dp))
                        .background(AlertRed)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = buildingSummary.ultimaActividad.titulo,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = buildingSummary.ultimaActividad.detalle,
                        fontSize = 12.sp,
                        color = TextSecondary
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
private fun StatItem(
    count: String,
    label: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}

@Composable
private fun FloorRowItem(
    floor: FloorSummary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Floor badge icon (e.g. PB, P1, P2, P3)
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(PlanRoomBg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = floor.codigo,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = floor.nombre,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = floor.detalle,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (floor.tieneAlerta) AlertRed else SuccessGreen
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}
