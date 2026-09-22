package com.christhperalta.activosfijos.feature.pdf.presentation


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christhperalta.activosfijos.feature.pdf.domain.PdfRepository
import kotlinx.coroutines.launch

class PdfViewModel (
    private val repository: PdfRepository
) : ViewModel() {

    var pdfBytes by mutableStateOf<ByteArray?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun loadPdf(url: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                pdfBytes = repository.getPdf(url).getOrThrow()
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }

}