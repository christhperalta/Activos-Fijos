package com.christhperalta.activosfijos.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.session_expired_alert_message
import com.christhperalta.activosfijos.core.presentation.SessionExpiredAlertDialog
import com.christhperalta.activosfijos.core.shared.UserNameRepository
import com.christhperalta.activosfijos.feature.home.domain.model.AssetCountSummary
import com.christhperalta.activosfijos.feature.home.presentation.components.HeroSummaryCard
import com.christhperalta.activosfijos.feature.home.presentation.components.ScannedAssetsCard
import com.christhperalta.activosfijos.feature.home.presentation.components.StatusGrid
import com.christhperalta.activosfijos.feature.inventory.domain.InventoryRepository
import com.christhperalta.activosfijos.feature.inventory.presentation.InventoryBottomBar
import com.christhperalta.activosfijos.feature.inventory.presentation.InventoryProgressCard
import com.christhperalta.activosfijos.feature.inventory.presentation.InventorySecondaryButton
import com.christhperalta.activosfijos.feature.inventory.presentation.InventorySurfaceCard
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onScanner: () -> Unit,
    onSummary: () -> Unit,
    onAssets: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
    inventoryRepository: InventoryRepository = koinInject(),
    userNameRepository: UserNameRepository = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Sigue viniendo de FakeInventoryRepository: no hay endpoint de activos escaneados todavía.
    val scannedAssets by inventoryRepository.scannedAssets.collectAsStateWithLifecycle()
    val userName = runCatching { userNameRepository.getName() }.getOrDefault("Juan Peralta")

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
                    FilledIconButton(
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        )
                    ) {
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

            val summary = uiState.summary
            when {
                uiState.isLoading && summary == null -> SummaryLoading()
                uiState.error != null && summary == null -> SummaryError(
                    message = uiState.error.orEmpty(),
                    onRetry = { viewModel.onEvent(HomeEvent.Retry) },
                )

                summary != null -> SummaryContent(
                    summary = summary,
                    error = uiState.error,
                    onRetry = { viewModel.onEvent(HomeEvent.Retry) },
                    onSummary = onSummary,
                )
            }

            ScannedAssetsCard(scannedAssets.size, onAssets)
        }
    }

    if (uiState.sessionExpired) {
        SessionExpiredAlertDialog(
            onConfirmation = {
                viewModel.onEvent(HomeEvent.SessionExpiredAccepted)
                onSessionExpired()
            },
            errorMessage = stringResource(Res.string.session_expired_alert_message),
        )
    }
}

@Composable
private fun SummaryLoading() {
    InventorySurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.fillMaxWidth().padding(vertical = 32.dp), Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun SummaryError(message: String, onRetry: () -> Unit) {
    InventorySurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            "No pudimos cargar el conteo",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start,
        )
        InventorySecondaryButton("Reintentar", onRetry, Modifier.fillMaxWidth())
    }
}

@Composable
private fun SummaryContent(
    summary: AssetCountSummary,
    error: String?,
    onRetry: () -> Unit,
    onSummary: () -> Unit,
) {
    HeroSummaryCard(summary)
    Text(
        "Estado de los activos",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold
    )
    StatusGrid(summary)
    InventoryProgressCard(
        verifiedCount = summary.verifiedCount,
        totalAssets = summary.totalAssets,
        progress = summary.progress,
        title = "Resumen del conteo",
        onClick = onSummary,
    )
    // Una recarga fallida sobre datos ya cargados no borra lo que el usuario está viendo:
    // se avisa y se ofrece reintentar debajo.
    if (error != null) {
        SummaryError(message = error, onRetry = onRetry)
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
