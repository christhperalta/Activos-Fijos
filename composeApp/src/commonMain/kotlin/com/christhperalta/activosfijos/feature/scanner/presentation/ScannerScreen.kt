package com.christhperalta.activosfijos.feature.scanner.presentation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.scanner_flashlight_description
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import qrscanner.CameraLens
import qrscanner.QrScanner

@Composable
fun ScannerScreen(
    onProduct: (String?,String?) -> Unit,
    viewModel: ScannerViewModel = koinInject()
) {
    var flashOn by remember { mutableStateOf(false) }
    var openGallery by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        QrScanner(
            modifier = Modifier.fillMaxSize(),
            flashlightOn = flashOn,
            openImagePicker = openGallery,
            cameraLens = CameraLens.Back,
            onCompletion = { result ->
                if (!isProcessing) {
                    isProcessing = true
                    viewModel.onButtonClick()
                    onProduct(null,result)
                }
            },
            imagePickerHandler = { openGallery = it },
            onFailure = { error ->
                println("QRScanner error: $error")
            }
        )

        IconButton(
            onClick = { flashOn = !flashOn },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) {
            Icon(
                imageVector = if (flashOn) Icons.Default.FlashlightOff
                else Icons.Default.FlashlightOn,
                contentDescription = stringResource(Res.string.scanner_flashlight_description),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}