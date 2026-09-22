package com.christhperalta.activosfijos

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
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
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 15_000
            socketTimeoutMillis = 15_000
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; isLenient = true })
        }
    }
}


