package com.christhperalta.activosfijos.feature.product.presentation

import com.christhperalta.activosfijos.core.quotation.Product


data class ProductUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
