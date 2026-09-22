package com.christhperalta.activosfijos.feature.product.data.repository

import com.christhperalta.activosfijos.core.quotation.Product
import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.feature.product.data.remote.api.ProductApiService
import com.christhperalta.activosfijos.feature.product.data.remote.dto.ProductValue
import com.christhperalta.activosfijos.feature.product.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val apiService: ProductApiService
) : ProductRepository {

    override suspend fun getProductByBarCode(productBarcode: String): Result<Product> {
        return try {
            val response = apiService.getProductByBarCode(productBarcode)
            val product = response.value.firstOrNull()?.toDomain()

            Result.success(product ?: Product())

        } catch (e: SapException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(productId: String): Result<Product> {
        return try {
            val response = apiService.getProductById(productId)
            val product = response.value.firstOrNull()?.toDomain()

            Result.success(product ?: Product())

        } catch (e: SapException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}

fun ProductValue.toDomain(): Product {
    return Product(

        itemCode = this.itemCode ?: "",
        itemDescription = this.itemName ?: "",
        whsCode = this.whsCode ?: "",
        unitPrice = (this.price?.times(100))?.toLong() ?: 0L,
        taxCode = this.uITBIS ?: ""
    )
}