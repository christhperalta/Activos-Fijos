package com.christhperalta.activosfijos

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.serialization.kotlinx.json.json
import com.christhperalta.activosfijos.core.constants.Constants
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

actual fun createHttpClient(): HttpClient {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit
        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
    })

    val sslContext = SSLContext.getInstance("SSL").apply {
        init(null, trustAllCerts, java.security.SecureRandom())
    }

    return HttpClient(OkHttp) {
        engine {
            preconfigured = OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                // Ktor sobreescribe estos valores desde HttpTimeout, pero los defaults de
                // OkHttp son 10 s: dejarlos explícitos evita depender de ese detalle.
                .connectTimeout(Constants.HTTP_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(Constants.HTTP_SOCKET_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .writeTimeout(Constants.HTTP_SOCKET_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .build()
        }
        install(HttpCookies)
        install(HttpTimeout) {
            requestTimeoutMillis = Constants.HTTP_REQUEST_TIMEOUT_MILLIS
            connectTimeoutMillis = Constants.HTTP_CONNECT_TIMEOUT_MILLIS
            socketTimeoutMillis = Constants.HTTP_SOCKET_TIMEOUT_MILLIS
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
}