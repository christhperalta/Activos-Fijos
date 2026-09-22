package com.christhperalta.activosfijos.feature.config.presentation



sealed class ConfigEvent {

    sealed class Manager : ConfigEvent() {
        data class OnUsernameChange(val username: String) : Manager()
        data class OnPasswordChange(val password: String) : Manager()
        data object SaveManagerConfig : Manager()
    }

    sealed class Server : ConfigEvent() {
        data class OnProtocolChange(val protocol: String) : Server()
        data class OnHostChange(val host: String) : Server()
        data class OnPortChange(val port: String) : Server()
        data class OnDatabaseNameChange(val database: String) : Server()
        data object TestConnection : Server()
        data object SaveConfig : Server()
    }

    sealed class Storage : ConfigEvent() {
        data class OnSellerChange(val name: String, val code: Int) : Storage()
        data class OnWarehouseChange(val name: String, val code: String) : Storage()
        data class OnCustomerChange(val name: String, val code: String?) : Storage()
        data class OnPriceListChange(val name: String, val code: String) : Storage()
        data class OnCardNameChange(val cardName: String) : Storage()
        data class OnCardCodeChange(val cardCode: String) : Storage()
        data class OnDocCurrencyChange(val currency: String) : Storage()
        data class OnTaxCodeChange(val taxCode: String) : Storage()
        data class OnTerminalNumberChange(val number: String) : Storage()
        data class OnRncChange(val rnc : String) : Storage()
        data object SaveStoreConfig: Storage()
    }
}

sealed class TestResult {
    data object Success : TestResult()
    data class Error(val message: String) : TestResult()
}

