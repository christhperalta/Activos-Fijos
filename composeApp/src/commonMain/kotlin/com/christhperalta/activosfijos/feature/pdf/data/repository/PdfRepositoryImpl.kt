package com.christhperalta.activosfijos.feature.pdf.data.repository

import com.christhperalta.activosfijos.feature.pdf.data.remote.api.PdfApiService
import com.christhperalta.activosfijos.feature.pdf.domain.PdfRepository

class PdfRepositoryImpl (
    private val apiService: PdfApiService
) : PdfRepository {
    override suspend fun getPdf(url: String): Result<ByteArray> {
        return try {
            val pdfBytes = apiService.fetchPdf(url)
            Result.success(pdfBytes)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

}