package com.christhperalta.activosfijos.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christhperalta.activosfijos.core.utils.getCurrentDate
import com.christhperalta.activosfijos.feature.config.domain.model.StoreConfigModel
import com.christhperalta.activosfijos.feature.config.domain.usecase.GetStoreConfigUseCase
import com.christhperalta.activosfijos.feature.home.domain.model.Quotations
import com.christhperalta.activosfijos.feature.home.domain.repository.HomeRepository
import com.christhperalta.activosfijos.feature.home.presentation.QuotationResult.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class HomeViewModel(
    private val getStoreConfig: GetStoreConfigUseCase,
    private val quotationRepository  : HomeRepository,
) : ViewModel() {

    private val _storeConfig = MutableStateFlow<StoreConfigModel?>(null)
    val storeConfig: StateFlow<StoreConfigModel?> = _storeConfig

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState : StateFlow<HomeUiState> = _uiState



    init {
        loadStoreConfig()
    }

    private fun loadStoreConfig() {
        viewModelScope.launch {
            val config = getStoreConfig()
            _storeConfig.value = config
           _uiState.update {
               it.copy(name = config?.cardName ?: "", sellerName = config?.sellerName ?: "")
           }
        }
    }

    private fun update(block: HomeUiState.() -> HomeUiState) {
        _uiState.value = _uiState.value.block()
    }

    fun resetFiscalInfo () {
        update { copy(rnc = "", comment = "", ncfType = "", documentType = "") }
    }

    fun resetQuotationResult() {
        update { copy(quotationResult = Idle) }
    }

    fun onEvent (event : HomeEvent) {
        when(event) {
            is HomeEvent.OnCreateQuotation -> {
                _uiState.update { state -> state.copy(isLoading = true, quotationResult = Idle) }
                val currentEvent = event.params
                viewModelScope.launch {
                   val result =  quotationRepository.quotation(Quotations(
                        cardCode = getStoreConfig()?.cardCode,
                        comments = currentEvent.comment,
                        docCurrency = getStoreConfig()?.docCurrency,
                        docDate = getCurrentDate(),
                        docDueDate = getCurrentDate(),
                        docObjectCode = "17",
                        docType = "rCustomer",
                        federalTaxID = currentEvent.rnc.ifBlank { getStoreConfig()?.rnc },
                        salesPersonCode = getStoreConfig()?.sellerCode.toString(),
                        uB1POS = "Y",
                        uB1POSCaja = getStoreConfig()?.terminalNumber,
                        uB1POSItem = "${currentEvent.products.size}",
                        uB1POST = getCurrentDate(),
                        uB1POSU = "B1Pos Mobil",
                        uFacFecha = getCurrentDate(),
                        uFacNit = currentEvent.rnc.ifBlank { getStoreConfig()?.rnc },
                        uFacNom = currentEvent.cardName,
                        cardName = currentEvent.cardName,
                        uFacSerie = currentEvent.facSerie,
                        uTNegocio = currentEvent.businessType,
                        documentLines = currentEvent.products
                    ), currentEvent.endPoint)

                    result.onSuccess { docNum ->
                        _uiState.update {
                            it.copy(
                                name = getStoreConfig()?.cardName ?: "",
                                quotationResult = Success(docNum),
                                isLoading = false
                            )
                        }
                    }.onFailure { error ->
                        _uiState.update {
                            it.copy(
                                error = error.message ?: "Error desconocido",
                                quotationResult = Error(error.message ?: "Error desconocido"),
                                isLoading = false
                            )
                        }
                    }
                }

            }
            is HomeEvent.OnValidateRnc -> {
                viewModelScope.launch {
                    quotationRepository.validateRnc(event.rnc)
                        .onSuccess { result ->
                            _uiState.update {
                                it.copy(
                                    rncValidated = result,
                                    name = result.name ?: "",
                                    rncStatus = if (result.estatus == "ACTIVO") RncValidationStatus.SUCCESS else RncValidationStatus.ERROR
                                )
                            }
                        }
                        .onFailure { error ->
                            _uiState.update {
                                it.copy(
                                    rncStatus = RncValidationStatus.ERROR,
                                    error = error.message ?: "Error validando el RNC"
                                )
                            }
                        }
                }
            }
            is HomeEvent.OnCommentChange -> {
                update { copy(comment = event.comment)}
            }
            is HomeEvent.OnDocumentTypeChange -> {
                update { copy(documentType = event.document)}
            }
            is HomeEvent.OnNameChange -> {
                update { copy(name = event.name)}
            }
            is HomeEvent.OnNcfChange -> {
                update {copy(ncfType = event.ncfType)}
            }
            is HomeEvent.OnRncChange -> {
                update { copy(rnc = event.rnc)}
            }
            is HomeEvent.OnProductIdChange -> {
                update{copy(productId = event.product)}
            }

            is HomeEvent.OnProductByNameChange -> {
                update{copy(productName = event.productName)}
            }
        }
    }


}

