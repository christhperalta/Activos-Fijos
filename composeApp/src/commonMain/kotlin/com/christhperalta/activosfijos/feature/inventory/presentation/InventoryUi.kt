package com.christhperalta.activosfijos.feature.inventory.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.christhperalta.activosfijos.feature.inventory.domain.AssetCondition

internal val InventoryCardShape = RoundedCornerShape(22.dp)
internal val InventoryInputShape = RoundedCornerShape(16.dp)
internal val GoodColor = Color(0xFF1B7A33)
internal val GoodContainer = Color(0xFFD3EED8)
internal val MaintenanceColor = Color(0xFF8A5A00)
internal val MaintenanceContainer = Color(0xFFFFE5AD)
internal val DecommissionColor = Color(0xFFB3261E)
internal val DecommissionContainer = Color(0xFFFFD4D3)
internal val NotFoundColor = Color(0xFF44566E)
internal val NotFoundContainer = Color(0xFFDCE4EE)
internal val PendingColor = Color(0xFF5C6B8A)
internal val PendingContainer = Color(0xFFE8ECF4)

@Composable
internal fun InventorySurfaceCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .clip(InventoryCardShape)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

@Composable
internal fun InventoryPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(50.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
internal fun InventorySecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = CircleShape,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondary),
    ) {
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
internal fun InventoryIconTile(icon: ImageVector, tint: Color = MaterialTheme.colorScheme.primary) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(22.dp))
    }
}

@Composable
internal fun InventoryBottomBar(
    scannerSelected: Boolean,
    onScanner: () -> Unit,
    onHome: () -> Unit = {},
) {
    Column {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
            val colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            NavigationBarItem(
                selected = !scannerSelected,
                onClick = onHome,
                icon = { Icon(Icons.Default.Home, "Inicio") },
                label = { Text("Inicio") },
                colors = colors,
            )
            NavigationBarItem(
                selected = scannerSelected,
                onClick = onScanner,
                icon = { Icon(Icons.Default.QrCodeScanner, "Escáner") },
                label = { Text("Escáner") },
                colors = colors,
            )
        }
    }
}

internal fun conditionLabel(condition: AssetCondition): String = when (condition) {
    AssetCondition.GOOD -> "Buen estado"
    AssetCondition.MAINTENANCE -> "Mantenimiento"
    AssetCondition.PENDING_DECOMMISSION -> "Pendiente de baja"
    AssetCondition.NOT_FOUND -> "No encontrado"
}

internal fun conditionColor(condition: AssetCondition): Color = when (condition) {
    AssetCondition.GOOD -> GoodColor
    AssetCondition.MAINTENANCE -> MaintenanceColor
    AssetCondition.PENDING_DECOMMISSION -> DecommissionColor
    AssetCondition.NOT_FOUND -> NotFoundColor
}

internal fun conditionContainer(condition: AssetCondition): Color = when (condition) {
    AssetCondition.GOOD -> GoodContainer
    AssetCondition.MAINTENANCE -> MaintenanceContainer
    AssetCondition.PENDING_DECOMMISSION -> DecommissionContainer
    AssetCondition.NOT_FOUND -> NotFoundContainer
}
