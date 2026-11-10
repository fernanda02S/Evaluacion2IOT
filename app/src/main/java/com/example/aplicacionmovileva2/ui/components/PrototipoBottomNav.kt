package com.example.aplicacionmovileva2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicacionmovileva2.ui.navigation.BottomTab
import com.example.aplicacionmovileva2.ui.navigation.Screen
import com.example.aplicacionmovileva2.ui.theme.BorderSubtle
import com.example.aplicacionmovileva2.ui.theme.DarkSlate
import com.example.aplicacionmovileva2.ui.theme.SurfaceWhite
import com.example.aplicacionmovileva2.ui.theme.TextMuted

@Composable
fun PrototipoBottomNav(
    currentScreen: Screen,
    onTabSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    // When on Credencial de apertura, the Incendio tab is highlighted as active
    val activeTab = when (currentScreen) {
        Screen.RESUMEN -> BottomTab.RESUMEN
        Screen.PLANO -> BottomTab.PLANO
        Screen.PUERTAS -> BottomTab.PUERTAS
        Screen.INCENDIO, Screen.CREDENCIAL_APERTURA -> BottomTab.INCENDIO
        else -> BottomTab.RESUMEN
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
    ) {
        HorizontalDivider(thickness = 1.dp, color = BorderSubtle)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomTab.values().forEach { tab ->
                val isSelected = tab == activeTab
                val itemColor = if (isSelected) DarkSlate else TextMuted

                Column(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onTabSelected(tab.route)
                        }
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (tab) {
                        BottomTab.RESUMEN -> IconResumenGrid(tint = itemColor, size = 20.dp)
                        BottomTab.PLANO -> IconPlanoMap(tint = itemColor, size = 20.dp)
                        BottomTab.PUERTAS -> IconPuertasDoor(tint = itemColor, size = 20.dp)
                        BottomTab.INCENDIO -> IconIncendioFlame(tint = itemColor, size = 20.dp)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = tab.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = itemColor
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    // Small indicator bar under selected tab
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .width(18.dp)
                                .height(2.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(DarkSlate)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }
        }
    }
}
