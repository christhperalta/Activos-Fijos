package com.christhperalta.activosfijos.feature.product.data.remote.api

import com.christhperalta.activosfijos.core.shared.SessionRepository
import com.christhperalta.activosfijos.core.utils.SapErrorResponse
import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.feature.config.domain.repository.ServerConfigRepository
import com.christhperalta.activosfijos.feature.config.domain.repository.StoreConfigRepository
import com.christhperalta.activosfijos.feature.product.data.remote.dto.ProductDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.isSuccess

class ProductApiService (
    private val client: HttpClient,
    private val serverConfigRepository: ServerConfigRepository,
    private val storeConfigRepository: StoreConfigRepository,
    private val sessionRepository: SessionRepository,
) {

    private suspend fun getBaseUrl(): String {
        val config = serverConfigRepository.getServerConfig()
            ?: throw IllegalStateException("No hay configuración de servidor guardada")
        return "${config.protocol}://${config.host}:${config.port}"
    }

        private suspend fun getDatabaseWarehouseCode(): String {
        val config = storeConfigRepository.getStoreConfig()
            ?: throw IllegalStateException("No hay configuración de tienda guardada")
        return config.warehouseCode.toString()
    }

    private suspend fun getPriceList(): String {
        val config = storeConfigRepository.getStoreConfig()
            ?: throw IllegalStateException("No hay configuración de tienda guardada")
        return config.priceCode.toString()
    }


    suspend fun getProductByBarCode(productBarCode : String): ProductDto {
        val baseUrl = getBaseUrl()
        val whCode = getDatabaseWarehouseCode()
        val priceList = getPriceList()
        val sessionId = sessionRepository.getSession()
        val response = client.get("$baseUrl/b1s/v1/view.svc/B1POS_OITMB1SLQuery?\$filter=(WhsCode eq '$whCode' and PriceList eq $priceList and CodeBars eq '${productBarCode}')"){
            header("Cookie", "B1SESSION=$sessionId")
        }
        if (!response.status.isSuccess()) {
            val errorBody = response.body<SapErrorResponse>()
            throw SapException(
                code = errorBody.error?.code,
                errorMessage = errorBody.error?.message?.value
            )
        }
        return response.body<ProductDto>()
    }

    suspend fun getProductById(productId : String): ProductDto {
        val baseUrl = getBaseUrl()
        val whCode = getDatabaseWarehouseCode()
        val priceList = getPriceList()
        val sessionId = sessionRepository.getSession()
        val response = client.get("$baseUrl/b1s/v1/view.svc/B1POS_OITMB1SLQuery?\$filter=(WhsCode eq '$whCode' and PriceList eq $priceList and ItemCode eq '$productId')"){
            header("Cookie", "B1SESSION=$sessionId")
        }
        if (!response.status.isSuccess()) {
            val errorBody = response.body<SapErrorResponse>()
            throw SapException(
                code = errorBody.error?.code,
                errorMessage = errorBody.error?.message?.value
            )
        }
        return response.body<ProductDto>()
    }


}