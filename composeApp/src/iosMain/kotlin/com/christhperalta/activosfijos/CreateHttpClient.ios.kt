package com.christhperalta.activosfijos

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import com.christhperalta.activosfijos.core.constants.Constants
import kotlinx.serialization.json.Json
import platform.Foundation.NSURLSessionAuthChallengePerformDefaultHandling

actual fun createHttpClient(): HttpClient {
    return HttpClient(engineFactory = Darwin) {
        engine {
            handleChallenge { _, _, _, completionHandler ->
                completionHandler(
                    NSURLSessionAuthChallengePerformDefaultHandling.toInt(),
                    null
                )
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = Constants.HTTP_REQUEST_TIMEOUT_MILLIS
            connectTimeoutMillis = Constants.HTTP_CONNECT_TIMEOUT_MILLIS
            socketTimeoutMillis = Constants.HTTP_SOCKET_TIMEOUT_MILLIS
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; isLenient = true })
        }
    }
}


