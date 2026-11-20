package com.christhperalta.activosfijos.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.christhperalta.activosfijos.feature.home.domain.model.AssetCountSummary
import com.christhperalta.activosfijos.feature.inventory.domain.AssetCondition
import com.christhperalta.activosfijos.feature.inventory.presentation.InventoryCardShape
import com.christhperalta.activosfijos.feature.inventory.presentation.conditionColor
import com.christhperalta.activosfijos.feature.inventory.presentation.conditionContainer

@Composable
internal fun StatusGrid(summary: AssetCountSummary) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatusCard("Buen estado", summary.goodCount, AssetCondition.GOOD, Modifier.weight(1f))
            StatusCard(
                "Mantenimiento",
                summary.maintenanceCount,
                AssetCondition.MAINTENANCE,
                Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatusCard(
                "Pendiente de baja",
                summary.pendingDecommissionCount,
                AssetCondition.PENDING_DECOMMISSION,
                Modifier.weight(1f)
            )
            // Sin endpoint que la alimente por ahora. El Spacer mantiene la rejilla cuadrada,
            // así que restaurarla es descomentar y quitarlo.
//            StatusCard(
//                "No encontrados",
//                summary.notFoundCount,
//                AssetCondition.NOT_FOUND,
//                Modifier.weight(1f)
//            )
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatusCard(label: String, count: Int, condition: AssetCondition, modifier: Modifier) {
    val color = conditionColor(condition)
    Box(
        modifier.clip(InventoryCardShape).background(conditionContainer(condition)).padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(Modifier.size(12.dp).clip(CircleShape).background(color))
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium,
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
