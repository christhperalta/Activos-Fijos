package com.christhperalta.activosfijos.feature.inventory.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.christhperalta.activosfijos.feature.inventory.domain.Asset
import com.christhperalta.activosfijos.feature.inventory.domain.AssetCondition
import com.christhperalta.activosfijos.feature.inventory.domain.AssetRegistration
import com.christhperalta.activosfijos.feature.inventory.domain.InventoryRepository
import org.koin.compose.koinInject

@Composable
fun ScannedAssetsScreen(
    onBack: () -> Unit,
    onAsset: (String) -> Unit,
    repository: InventoryRepository = koinInject(),
) {
    val scannedAssets by repository.scannedAssets.collectAsStateWithLifecycle()
    AssetScaffold("Activos escaneados", onBack) { padding ->
        if (scannedAssets.isEmpty()) {
            EmptyAssetContent(
                message = "Aún no has escaneado activos",
                subtitle = "Escanea un código QR o ingresa un código para comenzar.",
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(scannedAssets, key = { it.code }) { asset ->
                    val registration = repository.registrationFor(asset.code)
                    ScannedAssetRow(asset, registration) { onAsset(asset.code) }
                }
            }
        }
    }
}

@Composable
fun ScannedAssetDetailScreen(
    assetCode: String,
    onBack: () -> Unit,
    repository: InventoryRepository = koinInject(),
) {
    val asset = remember(assetCode) { repository.findAsset(assetCode) }
    val registration = remember(assetCode) { repository.registrationFor(assetCode) }
    AssetScaffold("Detalle del activo", onBack) { padding ->
        if (asset == null) {
            EmptyAssetContent("No encontramos el activo $assetCode")
        } else {
            Column(
                Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AssetIdentityCard(asset)
                InventorySurfaceCard {
                    AssetInfoRow(Icons.Default.Badge, "Código", asset.code)
                    AssetInfoRow(Icons.Default.Inventory2, "Número de serie", asset.serialNumber)
                    AssetInfoRow(Icons.Default.Computer, "Categoría", asset.category)
                    AssetInfoRow(Icons.Default.LocationOn, "Ubicación registrada", asset.location)
                    AssetInfoRow(Icons.Default.Person, "Responsable", asset.responsible, showDivider = false)
                }
                RegistrationStatusCard(registration)
            }
        }
    }
}

@Composable
private fun RegistrationStatusCard(registration: AssetRegistration?) {
    InventorySurfaceCard {
        Text("Estado en el conteo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        if (registration == null) {
            ScannedStatusPill(condition = null, text = "Registro pendiente")
            Text(
                "El activo fue escaneado pero aún no se completó su registro.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            ScannedStatusPill(condition = registration.condition, text = conditionLabel(registration.condition))
            registration.reason?.let { reason ->
                AssetInfoRow(Icons.Default.Info, "Motivo", reason)
            }
            if (registration.observations.isNotBlank()) {
                AssetInfoRow(Icons.Default.EditNote, "Observaciones", registration.observations, showDivider = false)
            }
            if (registration.hasPhoto) {
                Row(
                    Modifier.fillMaxWidth().clip(InventoryInputShape).background(GoodContainer).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(Icons.Default.CameraAlt, null, tint = GoodColor)
                    Column(Modifier.weight(1f)) {
                        Text("Fotografía adjunta", fontWeight = FontWeight.SemiBold)
                        Text("La evidencia se incluyó en el registro", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScannedAssetRow(asset: Asset, registration: AssetRegistration?, onClick: () -> Unit) {
    val condition = registration?.condition
    val color = if (condition != null) conditionColor(condition) else PendingColor
    InventorySurfaceCard(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        contentPadding = PaddingValues(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InventoryIconTile(Icons.Default.Computer, tint = color)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(asset.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(asset.code, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Medium)
                ScannedStatusPill(condition, if (condition != null) conditionLabel(condition) else "Registro pendiente")
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ScannedStatusPill(condition: AssetCondition?, text: String) {
    val color = if (condition != null) conditionColor(condition) else PendingColor
    val container = if (condition != null) conditionContainer(condition) else PendingContainer
    Row(
        Modifier.clip(CircleShape).background(container).padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(color))
        Text(text, color = color, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelMedium)
    }
}