package com.christhperalta.activosfijos.feature.home.presentation

import com.christhperalta.activosfijos.feature.home.domain.model.Rnc
import com.christhperalta.activosfijos.core.quotation.Product

const val Tax_Credit = "Credito Fiscal"

enum class RncValidationStatus { IDLE, SUCCESS, ERROR }

enum class DocumentType { RNC, CEDULA, PASAPORTE }

sealed class QuotationResult {
    data object Idle : QuotationResult()
    data class Success(val docNum: String) : QuotationResult()
    data class Error(val message: String) : QuotationResult()
}

data class HomeUiState(
    val rncValidated: Rnc? = null,
    val name: String = "",
    val rnc: String = "",
    val comment: String = "",
    val ncfType: String = "",
    val documentType: String = "",
    val productId: String = "",
    val productName : String = "",
    val rncStatus: RncValidationStatus = RncValidationStatus.IDLE,
    val sellerName : String = "",
    // Status
    val isLoading: Boolean = false,
    val error: String = "",
    val quotationResult: QuotationResult = QuotationResult.Idle
)


data class CreateQuotationParams(
    val comment: String,
    val cardName: String,
    val facSerie: String,
    val rnc: String,
    val businessType: String,
    val endPoint: String,
    val products: List<Product>
)