package com.christhperalta.activosfijos.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.christhperalta.activosfijos.feature.home.domain.model.AssetCountSummary
import com.christhperalta.activosfijos.feature.inventory.presentation.InventoryCardShape

@Composable
internal fun HeroSummaryCard(summary: AssetCountSummary) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(InventoryCardShape)
            .background(MaterialTheme.colorScheme.primary)
            .padding(20.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    Icons.Default.Inventory2,
                    null,
                    Modifier.size(20.dp),
                    Color.White.copy(alpha = 0.85f)
                )
                Text(
                    "ACTIVOS A CONTAR",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
            Text(
                summary.pendingToScanCount.toString(),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text("activos por contar", color = Color.White.copy(alpha = 0.85f))
        }
    }
}
