package com.christhperalta.activosfijos.feature.pdf.domain

interface PdfRepository {
    suspend fun getPdf (url: String): Result<ByteArray>
}