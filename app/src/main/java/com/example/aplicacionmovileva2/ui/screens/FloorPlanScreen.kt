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
import androidx.compose.material.icons.filled.Wifi
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
import com.example.aplicacionmovileva2.data.model.DoorData
import com.example.aplicacionmovileva2.data.model.DoorStatus
import com.example.aplicacionmovileva2.ui.components.AlertCountBadge
import com.example.aplicacionmovileva2.ui.components.PrimaryActionButton
import com.example.aplicacionmovileva2.ui.components.PrototipoFooter
import com.example.aplicacionmovileva2.ui.components.PrototipoHeader
import com.example.aplicacionmovileva2.ui.components.StatusBadge
import com.example.aplicacionmovileva2.ui.theme.AlertRed
import com.example.aplicacionmovileva2.ui.theme.AlertRedText
import com.example.aplicacionmovileva2.ui.theme.BackgroundLight
import com.example.aplicacionmovileva2.ui.theme.BorderLight
import com.example.aplicacionmovileva2.ui.theme.BorderSubtle
import com.example.aplicacionmovileva2.ui.theme.DarkSlate
import com.example.aplicacionmovileva2.ui.theme.PlanDoorBg
import com.example.aplicacionmovileva2.ui.theme.PlanRoomBg
import com.example.aplicacionmovileva2.ui.theme.PlanRoomBorder
import com.example.aplicacionmovileva2.ui.theme.SuccessGreen
import com.example.aplicacionmovileva2.ui.theme.SuccessGreenBorder
import com.example.aplicacionmovileva2.ui.theme.SurfaceWhite
import com.example.aplicacionmovileva2.ui.theme.TextMuted
import com.example.aplicacionmovileva2.ui.theme.TextPrimary
import com.example.aplicacionmovileva2.ui.theme.TextSecondary
import com.example.aplicacionmovileva2.ui.theme.WarningAmber
import com.example.aplicacionmovileva2.ui.theme.WarningAmberBorder

@Composable
fun FloorPlanScreen(
    selectedFloorIndex: Int,
    onFloorSelected: (Int) -> Unit,
    selectedDoorId: String,
    onDoorSelected: (String) -> Unit,
    doors: List<DoorData>,
    onNavigateToDoorDetail: (DoorData) -> Unit,
    modifier: Modifier = Modifier
) {
    val floorNames = listOf("Baja", "Planta 1", "Planta 2", "Planta 3")
    val floorPrefix = when (selectedFloorIndex) {
        0 -> "PB"
        1 -> "P1"
        2 -> "P2"
        else -> "P3"
    }

    val d01 = doors.find { it.id == "$floorPrefix-01" } ?: DoorData("$floorPrefix-01", "01", "Oficinas A", floorNames[selectedFloorIndex], DoorStatus.CERRADA)
    val d02 = doors.find { it.id == "$floorPrefix-02" } ?: DoorData("$floorPrefix-02", "02", "Sala técnica", floorNames[selectedFloorIndex], DoorStatus.CERRADA)
    val d03 = doors.find { it.id == "$floorPrefix-03" } ?: DoorData("$floorPrefix-03", "03", "Escalera B", floorNames[selectedFloorIndex], DoorStatus.CERRADA)
    val d04 = doors.find { it.id == "$floorPrefix-04" } ?: DoorData("$floorPrefix-04", "04", "Escalera A", floorNames[selectedFloorIndex], DoorStatus.ABIERTA)
    val d05 = doors.find { it.id == "$floorPrefix-05" } ?: DoorData("$floorPrefix-05", "05", "Oficinas B", floorNames[selectedFloorIndex], DoorStatus.CERRADA)
    val d06 = doors.find { it.id == "$floorPrefix-06" } ?: DoorData("$floorPrefix-06", "06", "Archivo", floorNames[selectedFloorIndex], DoorStatus.SIN_DATOS)

    val currentFloorDoors = listOf(d01, d02, d03, d04, d05, d06)
    val selectedDoor = doors.find { it.id == selectedDoorId } ?: currentFloorDoors.firstOrNull { it.id == "$floorPrefix-04" } ?: d01

    val alertsCount = currentFloorDoors.count { it.estado == DoorStatus.ABIERTA || it.estado == DoorStatus.SIN_DATOS }

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
                text = "Plano por planta",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Edificio Alameda · actualizado a las 09:41",
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Floor selector tabs
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BorderSubtle)
                .padding(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                floorNames.forEachIndexed { index, name ->
                    val isSelected = index == selectedFloorIndex
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) SurfaceWhite else Color.Transparent)
                            .clickable { onFloorSelected(index) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) TextPrimary else TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Schematic Floor Plan Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceWhite)
                .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedFloorIndex == 0) "Planta baja" else "Planta $selectedFloorIndex",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    AlertCountBadge(text = if (alertsCount > 0) "$alertsCount alertas" else "Sin alertas")
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "N ↑",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                }

                // Architectural Schematic Grid
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .border(1.5.dp, PlanRoomBorder, RoundedCornerShape(6.dp))
                        .background(PlanRoomBg)
                        .padding(1.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Top Rooms Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                        ) {
                            RoomBox(
                                name = d01.ubicacion,
                                modifier = Modifier.weight(1f),
                                borderRight = true
                            )
                            RoomBox(
                                name = d02.ubicacion,
                                modifier = Modifier.weight(1f),
                                borderRight = true
                            )
                            RoomBox(
                                name = d03.ubicacion,
                                modifier = Modifier.weight(1f),
                                borderRight = false
                            )
                        }

                        // Middle Corridor (PASILLO)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .border(width = 1.dp, color = PlanRoomBorder)
                                .background(SurfaceWhite),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "PASILLO",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted,
                                letterSpacing = 1.sp
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SchematicDoorChip(
                                    code = d01.numeroPlano,
                                    label = d01.estado.label,
                                    status = d01.estado,
                                    isSelected = selectedDoorId == d01.id,
                                    onClick = { onDoorSelected(d01.id) }
                                )
                                SchematicDoorChip(
                                    code = d02.numeroPlano,
                                    label = d02.estado.label,
                                    status = d02.estado,
                                    isSelected = selectedDoorId == d02.id,
                                    onClick = { onDoorSelected(d02.id) }
                                )
                                SchematicDoorChip(
                                    code = d03.numeroPlano,
                                    label = d03.estado.label,
                                    status = d03.estado,
                                    isSelected = selectedDoorId == d03.id,
                                    onClick = { onDoorSelected(d03.id) }
                                )
                            }
                        }

                        // Bottom Rooms Row
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                            ) {
                                RoomBox(
                                    name = d04.ubicacion,
                                    modifier = Modifier.weight(1f),
                                    borderRight = true,
                                    alignBottom = true
                                )
                                RoomBox(
                                    name = d05.ubicacion,
                                    modifier = Modifier.weight(1f),
                                    borderRight = true,
                                    alignBottom = true
                                )
                                RoomBox(
                                    name = d06.ubicacion,
                                    modifier = Modifier.weight(1f),
                                    borderRight = false,
                                    alignBottom = true
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.Top
                            ) {
                                SchematicDoorChip(
                                    code = d04.numeroPlano,
                                    label = d04.estado.label,
                                    status = d04.estado,
                                    isSelected = selectedDoorId == d04.id,
                                    onClick = { onDoorSelected(d04.id) }
                                )
                                SchematicDoorChip(
                                    code = d05.numeroPlano,
                                    label = d05.estado.label,
                                    status = d05.estado,
                                    isSelected = selectedDoorId == d05.id,
                                    onClick = { onDoorSelected(d05.id) }
                                )
                                SchematicDoorChip(
                                    code = d06.numeroPlano,
                                    label = d06.estado.label,
                                    status = d06.estado,
                                    isSelected = selectedDoorId == d06.id,
                                    onClick = { onDoorSelected(d06.id) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Vista esquemática de planta",
                    fontSize = 11.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Legend row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(color = SuccessGreen, label = "Cerrada")
            LegendItem(color = AlertRed, label = "Abierta")
            LegendItem(color = WarningAmber, label = "Sin datos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selected Door Detail Card
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${selectedDoor.id} · ${selectedDoor.ubicacion}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    StatusBadge(status = selectedDoor.estado)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = selectedDoor.detalleConexion,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Text(
                        text = selectedDoor.alerta,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (selectedDoor.estado) {
                            DoorStatus.ABIERTA -> AlertRedText
                            DoorStatus.SIN_DATOS -> WarningAmber
                            DoorStatus.CERRADA -> TextSecondary
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryActionButton(
                    text = "Ver detalle de puerta",
                    onClick = { onNavigateToDoorDetail(selectedDoor) }
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        PrototipoFooter()

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun RoomBox(
    name: String,
    modifier: Modifier = Modifier,
    borderRight: Boolean = true,
    alignBottom: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (borderRight) Modifier.border(
                    width = 0.8.dp,
                    color = PlanRoomBorder
                ) else Modifier
            )
            .padding(8.dp),
        contentAlignment = if (alignBottom) Alignment.BottomCenter else Alignment.TopCenter
    ) {
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SchematicDoorChip(
    code: String,
    label: String,
    status: DoorStatus,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val (bg, borderColor, textColor) = when (status) {
        DoorStatus.CERRADA -> Triple(PlanDoorBg, SuccessGreenBorder, SuccessGreen)
        DoorStatus.ABIERTA -> Triple(AlertRed, AlertRed, SurfaceWhite)
        DoorStatus.SIN_DATOS -> Triple(PlanDoorBg, WarningAmberBorder, WarningAmber)
    }

    Box(
        modifier = Modifier
            .width(52.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .border(
                width = if (isSelected) 2.dp else 1.2.dp,
                color = if (isSelected) DarkSlate else borderColor,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = code,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}
