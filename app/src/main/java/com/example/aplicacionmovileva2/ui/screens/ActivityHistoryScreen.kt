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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicacionmovileva2.data.model.ActivityItem
import com.example.aplicacionmovileva2.ui.components.PrototipoFooter
import com.example.aplicacionmovileva2.ui.components.PrototipoHeader
import com.example.aplicacionmovileva2.ui.theme.AlertRed
import com.example.aplicacionmovileva2.ui.theme.BackgroundLight
import com.example.aplicacionmovileva2.ui.theme.BorderLight
import com.example.aplicacionmovileva2.ui.theme.BorderSubtle
import com.example.aplicacionmovileva2.ui.theme.DarkSlate
import com.example.aplicacionmovileva2.ui.theme.SurfaceWhite
import com.example.aplicacionmovileva2.ui.theme.TextMuted
import com.example.aplicacionmovileva2.ui.theme.TextPrimary
import com.example.aplicacionmovileva2.ui.theme.TextSecondary

@Composable
fun ActivityHistoryScreen(
    activities: List<ActivityItem>,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("TODOS") }

    val filteredList = activities.filter { item ->
        when (selectedFilter) {
            "ALERTAS" -> item.titulo.contains("abierta", ignoreCase = true) || item.titulo.contains("alerta", ignoreCase = true) || item.titulo.contains("pérdida", ignoreCase = true)
            "USUARIOS" -> item.titulo.contains("usuario", ignoreCase = true) || item.titulo.contains("sesión", ignoreCase = true)
            else -> true
        }
    }

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
                text = "Historial de actividad",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Edificio Alameda · Registros persistentes del sistema",
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
                text = "Volver a Resumen",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkSlate
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Filter chips row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("TODOS" to "Todos", "ALERTAS" to "Alertas", "USUARIOS" to "Usuarios").forEach { (key, label) ->
                val isSelected = selectedFilter == key
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) DarkSlate else SurfaceWhite)
                        .border(1.dp, if (isSelected) DarkSlate else BorderLight, RoundedCornerShape(8.dp))
                        .clickable { selectedFilter = key }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) SurfaceWhite else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Activity List Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceWhite)
                .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay eventos registrados para este filtro", color = TextSecondary, fontSize = 13.sp)
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    filteredList.forEachIndexed { index, item ->
                        val isAlert = item.titulo.contains("abierta", ignoreCase = true) ||
                                item.titulo.contains("alerta", ignoreCase = true) ||
                                item.titulo.contains("incendio", ignoreCase = true) ||
                                item.titulo.contains("pérdida", ignoreCase = true)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = item.hora,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMuted,
                                modifier = Modifier.width(46.dp)
                            )

                            // Status bar
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(1.5.dp))
                                    .background(if (isAlert) AlertRed else DarkSlate)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.titulo,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.detalle,
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        if (index < filteredList.size - 1) {
                            HorizontalDivider(thickness = 0.8.dp, color = BorderSubtle)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        PrototipoFooter()

        Spacer(modifier = Modifier.height(24.dp))
    }
}
