package com.christhperalta.activosfijos.feature.product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christhperalta.activosfijos.feature.product.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ProductDetailViewModel (
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    fun setQuantity(value: Int) {
        _quantity.update { maxOf(1, value) }
    }

    fun fetchProduct(productId: String? = null, productBarcode : String? = null) {

        viewModelScope.launch {
            _uiState.update {
                it.copy(product = null, errorMessage = null, isLoading = true)
            }
            _quantity.update { 1 }
            val result = when {
                productBarcode != null -> productRepository.getProductByBarCode(productBarcode)
                productId != null -> productRepository.getProductById(productId)
                else -> Result.failure(IllegalArgumentException("Se requiere un identificador de activo"))
            }

            result
                .onSuccess { product ->
                    _uiState.update { it.copy(product = product, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(errorMessage = error.message, isLoading = false)
                    }
                }
        }
    }
}
