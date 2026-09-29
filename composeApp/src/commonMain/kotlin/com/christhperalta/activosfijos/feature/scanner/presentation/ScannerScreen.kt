package com.christhperalta.activosfijos.feature.scanner.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.christhperalta.activosfijos.feature.inventory.domain.InventoryRepository
import com.christhperalta.activosfijos.feature.inventory.presentation.InventoryBottomBar
import com.christhperalta.activosfijos.feature.inventory.presentation.InventoryInputShape
import com.christhperalta.activosfijos.feature.inventory.presentation.InventoryPrimaryButton
import com.christhperalta.activosfijos.feature.inventory.presentation.InventorySecondaryButton
import org.koin.compose.koinInject
import qrscanner.CameraLens
import qrscanner.QrScanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    onBack: () -> Unit,
    onHome: () -> Unit,
    onAsset: (String) -> Unit,
    viewModel: ScannerViewModel = koinInject(),
    repository: InventoryRepository = koinInject(),
) {
    var flashOn by remember { mutableStateOf(false) }
    var openGallery by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    var showManualEntry by remember { mutableStateOf(false) }
    var manualCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun processCode(rawCode: String) {
        val asset = repository.findAsset(rawCode)
        when {
            asset == null -> {
                errorMessage = "No encontramos un activo con el código ${rawCode.trim()}."
                isProcessing = false
            }
            else -> {
                repository.recordScan(asset.code)
                if (repository.registrationFor(asset.code) != null) {
                    errorMessage = "El activo ${asset.code} ya fue registrado en este conteo."
                    isProcessing = false
                } else {
                    viewModel.onButtonClick()
                    onAsset(asset.code)
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = { Text("Escanear activo", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
            )
        },
        bottomBar = { InventoryBottomBar(scannerSelected = true, onScanner = {}, onHome = onHome) },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            QrScanner(
                modifier = Modifier.fillMaxSize(),
                flashlightOn = flashOn,
                openImagePicker = openGallery,
                cameraLens = CameraLens.Back,
                onCompletion = { result -> if (!isProcessing) { isProcessing = true; processCode(result) } },
                imagePickerHandler = { openGallery = it },
                onFailure = { error -> errorMessage = "No fue posible iniciar el escáner: $error" },
            )
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.26f)))
            Column(
                Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(Modifier.size(52.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.94f)), contentAlignment = Alignment.Center) {
                        IconButton(onClick = { flashOn = !flashOn }) {
                            Icon(if (flashOn) Icons.Default.FlashlightOff else Icons.Default.FlashlightOn, "Linterna", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    InventoryPrimaryButton("Ingresar código", { showManualEntry = true }, Modifier.fillMaxWidth())
                }
            }
        }
    }
    if (showManualEntry) {
        AlertDialog(
            onDismissRequest = { showManualEntry = false },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(22.dp),
            title = { Text("Ingresar código", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Utiliza esta opción si no puedes leer el código QR.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    OutlinedTextField(
                        value = manualCode,
                        onValueChange = { manualCode = it },
                        label = { Text("Código del activo") },
                        singleLine = true,
                        shape = InventoryInputShape,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        ),
                    )
                }
            },
            confirmButton = { InventoryPrimaryButton("Continuar", { showManualEntry = false; processCode(manualCode) }, enabled = manualCode.isNotBlank()) },
            dismissButton = { InventorySecondaryButton("Cancelar", { showManualEntry = false }) },
        )
    }
    errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(22.dp),
            icon = { Icon(Icons.Default.ErrorOutline, null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("No se pudo continuar", fontWeight = FontWeight.Bold) },
            text = { Text(message) },
            confirmButton = { InventoryPrimaryButton("Entendido", { errorMessage = null }) },
        )
    }
}
