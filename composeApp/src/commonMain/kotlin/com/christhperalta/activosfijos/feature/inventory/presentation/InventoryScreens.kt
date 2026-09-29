package com.christhperalta.activosfijos.feature.inventory.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.christhperalta.activosfijos.feature.inventory.domain.Asset
import com.christhperalta.activosfijos.feature.inventory.domain.AssetCondition
import com.christhperalta.activosfijos.feature.inventory.domain.AssetRegistration
import com.christhperalta.activosfijos.feature.inventory.domain.InventoryRepository
import com.christhperalta.activosfijos.feature.inventory.domain.InventorySummary
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryHomeScreen(
    userName: String,
    onScanner: () -> Unit,
    onSummary: () -> Unit,
    onAssets: () -> Unit,
    repository: InventoryRepository = koinInject(),
) {
    val summary by repository.summary.collectAsStateWithLifecycle()
    val scannedAssets by repository.scannedAssets.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = userName.initials(),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = greetingForCurrentHour(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = userName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                },
                actions = {
                    FilledIconButton(onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        )) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Más opciones",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
            )
        },
        bottomBar = { InventoryBottomBar(scannerSelected = false, onScanner = onScanner) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                "Resumen del conteo",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            HeroSummaryCard(summary)
            Text(
                "Estado de los activos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            StatusGrid(summary)
            InventoryProgressCard(summary, title = "Resumen del conteo", onClick = onSummary)
            ScannedAssetsCard(scannedAssets.size, onAssets)
        }
    }
}

private fun greetingForCurrentHour(): String {
    return when (Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour) {
        in 5..11 -> "Buenos días!"
        in 12..18 -> "Buenas tardes!"
        else -> "Buenas noches!"
    }
}

private fun String.initials(): String {
    return trim()
        .split(Regex("\\s+"))
        .take(2)
        .joinToString("") { it.firstOrNull()?.uppercase() ?: "" }
}

@Composable
fun AssetInformationScreen(
    assetCode: String,
    onBack: () -> Unit,
    onConfirm: (String) -> Unit,
    repository: InventoryRepository = koinInject(),
) {
    val asset = remember(assetCode) { repository.findAsset(assetCode) }
    AssetScaffold("Información del activo", onBack) { padding ->
        if (asset == null) {
            EmptyAssetContent("No encontramos el activo $assetCode")
        } else {
            Column(
                Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AssetIdentityCard(asset)
                InventorySurfaceCard {
                    AssetInfoRow(Icons.Default.Badge, "Código", asset.code)
                    AssetInfoRow(Icons.Default.Inventory2, "Número de serie", asset.serialNumber)
                    AssetInfoRow(Icons.Default.Computer, "Categoría", asset.category)
                    AssetInfoRow(Icons.Default.LocationOn, "Ubicación registrada", asset.location)
                    AssetInfoRow(
                        Icons.Default.Person,
                        "Responsable",
                        asset.responsible,
                        showDivider = false
                    )
                }
                InventoryPrimaryButton(
                    "Confirmar activo",
                    { onConfirm(asset.code) },
                    Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun VerifyAssetScreen(
    assetCode: String,
    onBack: () -> Unit,
    onGood: (String) -> Unit,
    onMaintenance: (String) -> Unit,
    onDecommission: (String) -> Unit,
    repository: InventoryRepository = koinInject(),
) {
    val asset = remember(assetCode) { repository.findAsset(assetCode) }
    AssetScaffold("Verificar activo", onBack) { padding ->
        if (asset == null) EmptyAssetContent("Activo no encontrado") else Column(
            Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CompactAssetHeader(asset)
            Text(
                "¿Cuál es el estado del activo?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Selecciona la condición observada durante el conteo.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            ConditionOption(AssetCondition.GOOD, "Buen estado", "Funciona correctamente") {
                onGood(
                    asset.code
                )
            }
            ConditionOption(
                AssetCondition.MAINTENANCE,
                "Mantenimiento",
                "Necesita revisión o reparación"
            ) { onMaintenance(asset.code) }
            ConditionOption(
                AssetCondition.PENDING_DECOMMISSION,
                "Pendiente de baja",
                "No debe continuar en operación"
            ) { onDecommission(asset.code) }
        }
    }
}

@Composable
fun ConditionFormScreen(
    assetCode: String,
    condition: AssetCondition,
    onBack: () -> Unit,
    onRegistered: (String) -> Unit,
    repository: InventoryRepository = koinInject(),
) {
    val asset = remember(assetCode) { repository.findAsset(assetCode) }
    val title =
        if (condition == AssetCondition.MAINTENANCE) "Mantenimiento" else "Pendiente de baja"
    val reasons = if (condition == AssetCondition.MAINTENANCE) {
        listOf(
            "Daño físico",
            "Problema de software",
            "Problema de hardware",
            "Mantenimiento preventivo",
            "Otro"
        )
    } else listOf("Equipo dañado", "Equipo obsoleto", "No reparable", "Fin de vida útil", "Otro")
    var reason by remember { mutableStateOf<String?>(null) }
    var observations by remember { mutableStateOf("") }
    var hasPhoto by remember { mutableStateOf(false) }
    AssetScaffold(title, onBack) { padding ->
        if (asset == null) EmptyAssetContent("Activo no encontrado") else Column(
            Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CompactAssetHeader(asset)
            InventorySurfaceCard {
                Text(
                    if (condition == AssetCondition.MAINTENANCE) "Motivo del mantenimiento" else "Motivo de baja",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                reasons.forEach { option ->
                    ReasonOption(option, option == reason) {
                        reason = option
                    }
                }
            }
            InventorySurfaceCard {
                Text(
                    "Observaciones",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedTextField(
                    value = observations,
                    onValueChange = { observations = it },
                    modifier = Modifier.fillMaxWidth().height(136.dp),
                    label = { Text("Describe el estado del activo") },
                    supportingText = if (reason == "Otro" && observations.isBlank()) {
                        { Text("Describe el motivo seleccionado") }
                    } else null,
                    shape = InventoryInputShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                )
                PhotoAttachment(hasPhoto) { hasPhoto = !hasPhoto }
            }
            InventoryPrimaryButton(
                text = if (condition == AssetCondition.MAINTENANCE) "Registrar mantenimiento" else "Registrar pendiente de baja",
                onClick = {
                    if (reason != null && (reason != "Otro" || observations.isNotBlank())) {
                        repository.register(
                            asset.code,
                            AssetRegistration(condition, reason, observations, hasPhoto)
                        )
                        onRegistered(asset.code)
                    }
                },
                enabled = reason != null && (reason != "Otro" || observations.isNotBlank()),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun CountConfirmationScreen(
    assetCode: String,
    onScanNext: () -> Unit,
    onSummary: () -> Unit,
    repository: InventoryRepository = koinInject(),
) {
    val registration = remember(assetCode) { repository.registrationFor(assetCode) }
    val asset = remember(assetCode) { repository.findAsset(assetCode) }
    val condition = registration?.condition ?: AssetCondition.GOOD
    val scale by animateFloatAsState(1f, animationSpec = tween(350), label = "confirmationScale")
    Column(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            Modifier.size(104.dp).scale(scale).clip(CircleShape).background(GoodContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.CheckCircle, "Registro completado", Modifier.size(62.dp), GoodColor)
        }
        Spacer(Modifier.height(28.dp))
        Text(
            "Activo registrado",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))
        Box(
            Modifier.clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                asset?.code ?: assetCode,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(12.dp))
        StatusBadge(condition)
        Spacer(Modifier.height(16.dp))
        Text(
            "El activo fue registrado correctamente en el conteo.",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))
        InventoryPrimaryButton("Escanear siguiente", onScanNext, Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        InventorySecondaryButton("Ver resumen", onSummary, Modifier.fillMaxWidth())
    }
}

@Composable
fun CountSummaryScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit,
    repository: InventoryRepository = koinInject(),
) {
    val summary by repository.summary.collectAsStateWithLifecycle()
    var showPending by remember { mutableStateOf(false) }
    AssetScaffold("Resumen del conteo", onBack) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState())
                .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Conteo de activos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            InventoryProgressCard(summary)
            StatusRows(summary)
            if (summary.pendingCount > 0) {
                InventorySecondaryButton(
                    "Revisar ${summary.pendingCount} activos pendientes",
                    { showPending = true },
                    Modifier.fillMaxWidth()
                )
            }
            InventoryPrimaryButton("Continuar conteo", onContinue, Modifier.fillMaxWidth())
        }
    }
    if (showPending) PendingAssetsDialog(
        repository.pendingAssets(),
        { showPending = false },
        { repository.markNotFound(it) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AssetScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Volver",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
            )
        },
        content = content,
    )
}

@Composable
private fun HeroSummaryCard(summary: InventorySummary) {
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
                summary.totalAssets.toString(),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text("activos registrados", color = Color.White.copy(alpha = 0.85f))
        }
    }
}

@Composable
private fun StatusGrid(summary: InventorySummary) {
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
            StatusCard(
                "No encontrados",
                summary.notFoundCount,
                AssetCondition.NOT_FOUND,
                Modifier.weight(1f)
            )
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

@Composable
private fun ScannedAssetsCard(count: Int, onClick: () -> Unit) {
    InventorySurfaceCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InventoryIconTile(Icons.Default.QrCodeScanner)
            Column(Modifier.weight(1f)) {
                Text("Activos escaneados", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    if (count == 0) "Aún no has escaneado activos" else "Has leído $count activos en esta sesión",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Ver activos escaneados", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
private fun InventoryProgressCard(
    summary: InventorySummary,
    title: String = "Conteo actual",
    onClick: (() -> Unit)? = null
) {
    val cardModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    InventorySurfaceCard(modifier = cardModifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {

            Row(
                modifier = Modifier.weight(1f).padding(vertical = 8.dp, horizontal = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(bottom = 10.dp)) {
                            Text(
                                title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "${summary.verifiedCount} de ${summary.totalAssets} activos verificados",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Text(
                            "${(summary.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    LinearProgressIndicator(
                        progress = { summary.progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }

            }


                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    "Ver resumen del conteo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp).weight(0.1f)
                )


        }

    }
}
@Composable
internal fun AssetIdentityCard(asset: Asset) {
    InventorySurfaceCard {
        Box(
            Modifier.size(72.dp).clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Computer,
                null,
                Modifier.size(38.dp),
                MaterialTheme.colorScheme.primary
            )
        }
        Text(
            asset.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Box(
            Modifier.clip(CircleShape).background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                asset.code,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CompactAssetHeader(asset: Asset) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InventoryIconTile(Icons.Default.Computer)
        Column(Modifier.weight(1f)) {
            Text(
                asset.code,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                asset.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
internal fun AssetInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    showDivider: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InventoryIconTile(icon)
        Column(Modifier.weight(1f)) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
    if (showDivider) HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
}

@Composable
private fun ConditionOption(
    condition: AssetCondition,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(InventoryCardShape)
            .background(conditionContainer(condition)).clickable(onClick = onClick).padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            Modifier.size(42.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.75f)),
            contentAlignment = Alignment.Center
        ) {
            Box(Modifier.size(12.dp).clip(CircleShape).background(conditionColor(condition)))
        }
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = conditionColor(condition))
    }
}

@Composable
private fun ReasonOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).clickable(onClick = onClick)
            .padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(26.dp).clip(CircleShape)
                .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (selected) Icon(Icons.Default.Check, null, Modifier.size(16.dp), Color.White)
            else Box(
                Modifier.size(24.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(label, Modifier.weight(1f))
    }
}

@Composable
private fun PhotoAttachment(hasPhoto: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(InventoryInputShape)
            .background(if (hasPhoto) GoodContainer else MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        InventoryIconTile(
            Icons.Default.CameraAlt,
            if (hasPhoto) GoodColor else MaterialTheme.colorScheme.primary
        )
        Column(Modifier.weight(1f)) {
            Text(
                if (hasPhoto) "Fotografía adjunta" else "Agregar fotografía",
                fontWeight = FontWeight.SemiBold
            )
            Text(
                if (hasPhoto) "La evidencia se incluirá en el registro" else "Opcional para este registro",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (hasPhoto) Icon(Icons.Default.CheckCircle, null, tint = GoodColor)
    }
}

@Composable
internal fun StatusBadge(condition: AssetCondition) {
    Row(
        Modifier.clip(CircleShape).background(conditionContainer(condition))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(conditionColor(condition)))
        Text(
            conditionLabel(condition),
            color = conditionColor(condition),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun StatusRows(summary: InventorySummary) {
    InventorySurfaceCard(contentPadding = PaddingValues(0.dp)) {
        Column {
            StatusRow("Buen estado", summary.goodCount, AssetCondition.GOOD)
            StatusRow("Mantenimiento", summary.maintenanceCount, AssetCondition.MAINTENANCE)
            StatusRow(
                "Pendiente de baja",
                summary.pendingDecommissionCount,
                AssetCondition.PENDING_DECOMMISSION
            )
            StatusRow(
                "No encontrados",
                summary.notFoundCount,
                AssetCondition.NOT_FOUND,
                divider = false
            )
        }
    }
}

@Composable
private fun StatusRow(
    label: String,
    value: Int,
    condition: AssetCondition,
    divider: Boolean = true
) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(10.dp).clip(CircleShape).background(conditionColor(condition)))
        Spacer(Modifier.width(12.dp))
        Text(label, Modifier.weight(1f), fontWeight = FontWeight.Medium)
        Text(
            "$value activos",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
    if (divider) HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
}

@Composable
private fun PendingAssetsDialog(
    assets: List<Asset>,
    onDismiss: () -> Unit,
    onMarkNotFound: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = InventoryCardShape,
        title = { Text("Activos pendientes", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (assets.isEmpty()) Text("No hay activos pendientes.")
                assets.forEach { asset ->
                    Row(
                        Modifier.fillMaxWidth().clip(InventoryInputShape)
                            .background(NotFoundContainer).clickable { onMarkNotFound(asset.code) }
                            .padding(12.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.SearchOff, null, tint = NotFoundColor)
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                asset.code,
                                fontWeight = FontWeight.SemiBold
                            ); Text(
                            "Marcar como no encontrado",
                            style = MaterialTheme.typography.bodySmall
                        )
                        }
                    }
                }
            }
        },
        confirmButton = { InventorySecondaryButton("Cerrar", onDismiss) },
    )
}

@Composable
internal fun EmptyAssetContent(message: String, subtitle: String? = null) {
    Column(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier.size(72.dp).clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.SearchOff,
                null,
                Modifier.size(36.dp),
                MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(message, textAlign = TextAlign.Center, style = MaterialTheme.typography.titleMedium)
        if (subtitle != null) {
            Spacer(Modifier.height(8.dp))
            Text(subtitle, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
