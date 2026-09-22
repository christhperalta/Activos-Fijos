package com.christhperalta.activosfijos.feature.config.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christhperalta.activosfijos.feature.config.domain.model.ManagerCredentialConfigModel
import com.christhperalta.activosfijos.feature.config.domain.model.ServerConfigModel
import com.christhperalta.activosfijos.feature.config.domain.model.StoreConfigModel
import com.christhperalta.activosfijos.feature.config.domain.repository.ServerConfigRepository
import com.christhperalta.activosfijos.feature.config.domain.repository.StoreConfigRepository
import com.christhperalta.activosfijos.feature.config.domain.repository.ManagerCredentialsConfigRepository
import com.christhperalta.activosfijos.feature.login.domain.use_case.LogInUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ConfigViewModel(
    private val serverRepo: ServerConfigRepository,
    private val storeRepo: StoreConfigRepository,
    private val managerRepo: ManagerCredentialsConfigRepository,
    private val logInUserUseCase: LogInUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigUiState())
    val uiState: StateFlow<ConfigUiState> = _uiState.asStateFlow()

    private var storeSnapshot: StoreConfigModel? = null

    init {
        loadConfigs()
        _uiState.value
            .takeIf { it.port != null && it.host != null && it.protocol != null }
            ?.let { viewModelScope.launch { loadDropdownLists() } }
    }

    private fun loadConfigs() {
        viewModelScope.launch {
            update { copy(isLoading = true) }

            managerRepo.getManagerCredentialConfig()?.let { s ->
                update {
                    copy(
                        username = s.username,
                        password = s.password
                    )
                }
            }

            // Cargar config del servidor
            serverRepo.getServerConfig()?.let { s ->
                update {
                    copy(
                        protocol = s.protocol,
                        host = s.host,
                        port = s.port,
                        databaseName = s.databaseName
                    )
                }
            }

            // Cargar config de la tienda
            storeRepo.getStoreConfig()?.also { store ->
                storeSnapshot = store
                update {
                    copy(
                        terminalNumber = store.terminalNumber,
                        cardName = store.cardName,
                        cardCode = store.cardCode,
                        docCurrency = store.docCurrency,
                        taxCode = store.taxCode,
                        sellerCode = store.sellerCode,
                        sellerName = store.sellerName,
                        warehouseCode = store.warehouseCode,
                        warehouseName = store.warehouseName,
                        customerCode = store.customerCode,
                        customerName = store.customerName,
                        priceListCode = store.priceCode,
                        priceListName = store.priceName,
                        rnc = store.rnc
                    )
                }

                // Si hay config guardada, intentar cargar las listas
                loadDropdownLists()
            }

            update { copy(isLoading = false) }
        }
    }

    private suspend fun loadDropdownLists() {
        // Login silencioso para obtener las listas
        val managerCredential = managerRepo.getManagerCredentialConfig()
        val userLogIn =
            logInUserUseCase(managerCredential?.username ?: "", managerCredential?.password ?: "")
        if (userLogIn.isSuccess) {
            val sellerList = storeRepo.getSeller()
            val warehouseList = storeRepo.getWarehouse()
            val customerList = storeRepo.getCustomers()
            val priceList = storeRepo.getPriceList()

            update {
                copy(
                    sellerList = sellerList.getOrNull() ?: emptyList(),
                    warehouseList = warehouseList.getOrNull() ?: emptyList(),
                    customerList = customerList.getOrNull() ?: emptyList(),
                    priceList = priceList.getOrNull() ?: emptyList(),
                )
            }
        }
    }

    private fun update(block: ConfigUiState.() -> ConfigUiState) {
        _uiState.value = _uiState.value.block()
    }

    fun managerEvents(event: ConfigEvent.Manager) {
        when (event) {
            is ConfigEvent.Manager.OnUsernameChange -> {
                update { copy(username = event.username) }
            }

            is ConfigEvent.Manager.OnPasswordChange -> {
                update { copy(password = event.password) }
            }

            ConfigEvent.Manager.SaveManagerConfig -> {
                viewModelScope.launch {
                    runCatching {
                        managerRepo.saveManagerCredentialConfig(
                            ManagerCredentialConfigModel(
                                username = _uiState.value.username ?: "",
                                password = _uiState.value.password ?: ""
                            )
                        )
                    }.onSuccess {
                        update { copy(isManagerSaved = true, error = null) }
                    }.onFailure {
                        update { copy(error = it.message) }
                    }
                }
            }
        }
    }

    fun serverEvents(event: ConfigEvent.Server) {
        when (event) {
            is ConfigEvent.Server.OnProtocolChange -> {
                update { copy(protocol = event.protocol, isServerSaved = false, testResult = null) }
            }

            is ConfigEvent.Server.OnPortChange -> {
                update { copy(port = event.port, isServerSaved = false, testResult = null) }
            }

            is ConfigEvent.Server.OnHostChange -> {
                update { copy(host = event.host, isServerSaved = false, testResult = null) }
            }

            is ConfigEvent.Server.OnDatabaseNameChange -> {
                update { copy(databaseName = event.database, isServerSaved = false) }
            }

            ConfigEvent.Server.TestConnection -> {
                viewModelScope.launch {
                    update { copy(isTesting = true, testResult = null) }
                    val config = ServerConfigModel(
                        protocol = _uiState.value.protocol ?: "",
                        host = _uiState.value.host ?: "",
                        port = _uiState.value.port ?: "",
                        databaseName = _uiState.value.databaseName ?: "",
                    )
                    serverRepo.testConnection(config)
                        .onSuccess {
                            update { copy(testResult = TestResult.Success) }
                        }
                        .onFailure {
                            update {
                                copy(
                                    testResult = TestResult.Error(
                                        it.message ?: "Error de conexión"
                                    )
                                )
                            }
                        }
                    update { copy(isTesting = false) }
                }
            }

            ConfigEvent.Server.SaveConfig -> {
                viewModelScope.launch {
                    runCatching {
                        serverRepo.saveServerConfig(
                            ServerConfigModel(
                                protocol = _uiState.value.protocol ?: "",
                                host = _uiState.value.host ?: "",
                                port = _uiState.value.port ?: "",
                                databaseName = _uiState.value.databaseName ?: ""
                            )
                        )
                    }.onSuccess {
                        update { copy(isServerSaved = true, error = null) }
                        if (_uiState.value.isServerSaved) {
                            loadDropdownLists()
                        }
                    }.onFailure {
                        update { copy(error = it.message) }
                    }
                }
            }
        }
    }

    fun storageEvents(event: ConfigEvent.Storage) {
        when (event) {
            is ConfigEvent.Storage.OnSellerChange -> {
                update {
                    copy(
                        sellerName = event.name,
                        sellerCode = event.code,
                        isStoreSaved = false
                    )
                }
            }

            is ConfigEvent.Storage.OnWarehouseChange -> {
                update {
                    copy(
                        warehouseName = event.name,
                        warehouseCode = event.code,
                        isStoreSaved = false
                    )
                }
            }

            is ConfigEvent.Storage.OnCardCodeChange -> {
                update { copy(cardCode = event.cardCode, isStoreSaved = false) }
            }

            is ConfigEvent.Storage.OnCardNameChange -> {
                update { copy(cardName = event.cardName, isStoreSaved = false) }
            }

            is ConfigEvent.Storage.OnCustomerChange -> {
                update {
                    copy(
                        customerName = event.name,
                        customerCode = event.code,
                        isStoreSaved = false
                    )
                }

            }

            is ConfigEvent.Storage.OnDocCurrencyChange -> {
                update { copy(docCurrency = event.currency, isStoreSaved = false) }
            }

            is ConfigEvent.Storage.OnPriceListChange -> {
                update {
                    copy(
                        priceListName = event.name,
                        priceListCode = event.code,
                        isStoreSaved = false
                    )
                }
            }

            is ConfigEvent.Storage.OnRncChange -> {
                update { copy(rnc = event.rnc, isServerSaved = false) }
            }

            is ConfigEvent.Storage.OnTaxCodeChange -> {
                update { copy(taxCode = event.taxCode, isStoreSaved = false) }
            }

            is ConfigEvent.Storage.OnTerminalNumberChange -> {
                update { copy(terminalNumber = event.number, isStoreSaved = false) }
            }

            ConfigEvent.Storage.SaveStoreConfig -> {
                viewModelScope.launch {
                    val s = _uiState.value
                    val config = StoreConfigModel(
                        terminalNumber = s.terminalNumber,
                        cardName = s.cardName,
                        cardCode = s.cardCode,
                        docCurrency = s.docCurrency,
                        taxCode = s.taxCode,
                        sellerCode = s.sellerCode,
                        sellerName = s.sellerName,
                        warehouseCode = s.warehouseCode,
                        warehouseName = s.warehouseName,
                        customerCode = s.customerCode,
                        customerName = s.customerName,
                        priceCode = s.priceListCode,
                        priceName = s.priceListName,
                        rnc = s.rnc
                    )
                    runCatching {
                        storeRepo.saveStoreConfig(config)
                    }.onSuccess {
                        storeSnapshot = config
                        update { copy(isStoreSaved = true, error = null) }
                    }.onFailure {
                        update { copy(error = it.message) }
                    }
                }
            }
        }
    }


}