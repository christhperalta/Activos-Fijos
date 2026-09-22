package com.christhperalta.activosfijos.feature.pdf.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PdfApiService (
    private val client: HttpClient

) {

    suspend fun fetchPdf(url: String): ByteArray {
        return client.get(url).body<ByteArray>()
    }


}