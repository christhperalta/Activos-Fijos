package com.christhperalta.activosfijos.feature.product.domain.repository

import com.christhperalta.activosfijos.core.quotation.Product


interface ProductRepository {
    suspend fun getProductByBarCode(productBarcode : String): Result<Product>
    suspend fun getProductById(productId : String): Result<Product>
}