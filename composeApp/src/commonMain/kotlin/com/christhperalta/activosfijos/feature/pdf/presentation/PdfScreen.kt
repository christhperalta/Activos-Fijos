package com.christhperalta.activosfijos.feature.pdf.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import activosfijos.composeapp.generated.resources.Res
import activosfijos.composeapp.generated.resources.pdf_error_message
import com.dshatz.pdfmp.compose.PdfView
import com.dshatz.pdfmp.compose.state.DisplayState
import com.dshatz.pdfmp.compose.state.rememberPdfState
import com.dshatz.pdfmp.source.PdfSource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun PdfScreen(
    url: String,
    viewModel: PdfViewModel = koinInject()
) {
    LaunchedEffect(url) {
        viewModel.loadPdf(url)
    }



    Box(modifier = Modifier.fillMaxSize()) {
        when {
            viewModel.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            viewModel.error != null -> {
                Text(
                    text = stringResource(Res.string.pdf_error_message, viewModel.error ?: ""),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            viewModel.pdfBytes != null -> {
                val pdfBytes = remember(viewModel.pdfBytes) {
                    PdfSource.PdfBytes(viewModel.pdfBytes!!)
                }

                val pdfState = rememberPdfState(pdfBytes, pageSpacing = 8.dp)
                val displayState by pdfState.displayState  // ← by, no =

                when (displayState) {
                    is DisplayState.Initializing -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is DisplayState.Error -> {
                        Text(
                            text = stringResource(Res.string.pdf_error_message, (displayState as DisplayState.Error).error),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is DisplayState.Active -> {
                        PdfView(
                            state = pdfState,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}