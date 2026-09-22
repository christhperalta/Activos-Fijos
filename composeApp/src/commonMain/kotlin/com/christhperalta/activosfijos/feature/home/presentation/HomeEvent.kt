package com.christhperalta.activosfijos.feature.home.presentation




sealed class HomeEvent {
    data class OnNameChange (val name : String) : HomeEvent()
    data class OnRncChange (val rnc : String) : HomeEvent()
    data class OnCommentChange (val comment : String ) : HomeEvent()
    data class OnNcfChange (val ncfType : String ) : HomeEvent()
    data class OnDocumentTypeChange (val document : String) : HomeEvent()
    data class OnProductIdChange (val product : String) : HomeEvent()
    data class OnProductByNameChange(val productName : String) : HomeEvent()
    data class OnValidateRnc (val rnc : String) : HomeEvent()
    data class OnCreateQuotation(val params: CreateQuotationParams) : HomeEvent()
}
