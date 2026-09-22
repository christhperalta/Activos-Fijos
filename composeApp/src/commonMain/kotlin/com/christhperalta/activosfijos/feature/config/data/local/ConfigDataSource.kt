package com.christhperalta.activosfijos.feature.config.data.local

import com.christhperalta.activosfijos.database.AppDatabase
import com.christhperalta.activosfijos.feature.config.domain.model.ManagerCredentialConfigModel
import com.christhperalta.activosfijos.feature.config.domain.model.ServerConfigModel
import com.christhperalta.activosfijos.feature.config.domain.model.StoreConfigModel

class ConfigDataSource(database: AppDatabase) {

    private val queries = database.appDatabaseQueries

    // Manager Credentials
    fun getManagerCredentials(): ManagerCredentialConfigModel? =
        queries.getManagerCredentialsConfig().executeAsOneOrNull()?.let {
            ManagerCredentialConfigModel(username = it.username, password = it.password)
        }


    fun saveMangerCredentials(managerCredentials : ManagerCredentialConfigModel) {
        queries.upsertManagerCredentials(
            username = managerCredentials.username ?: "", password = managerCredentials.password ?: ""
        )
    }


    // Server
    fun getServerConfig(): ServerConfigModel? =
        queries.getServerConfig().executeAsOneOrNull()?.let {
            ServerConfigModel(protocol = it.protocol, host = it.host, port = it.port, databaseName = it.database_name)
        }

    fun saveServerConfig(config: ServerConfigModel) {
        queries.upsertServer(
            protocol = config.protocol,
            host = config.host,
            port = config.port,
            database_name = config.databaseName
        )
    }

    // Store
    fun getStoreConfig(): StoreConfigModel? =
        queries.getStoreConfig().executeAsOneOrNull()?.let {
            StoreConfigModel(
                terminalNumber = it.terminal_number,
                cardCode = it.card_code,
                cardName = it.card_name,
                docCurrency = it.doc_currency,
                taxCode = it.tax_code,
                sellerCode = it.seller_code.toIntOrNull(),
                sellerName = it.seller_name,
                warehouseCode = it.warehouse_code,
                warehouseName = it.warehouse_name,
                customerCode = it.customer_code,
                customerName = it.customer_name,
                priceCode = it.price_code,
                priceName = it.price_name,
                rnc =   it.rnc

            )
        }

    fun saveStoreConfig(config: StoreConfigModel) {
        queries.upsertStore(
            terminal_number = config.terminalNumber ?: "",
            card_code = config.cardCode ?: "",
            card_name = config.cardName ?: "",
            doc_currency = config.docCurrency ?: "",
            tax_code = config.taxCode ?: "",
            seller_code = config.sellerCode.toString(),
            seller_name = config.sellerName ?: "",
            warehouse_code = config.warehouseCode.toString(),
            warehouse_name = config.warehouseName ?: "",
            customer_code = config.customerCode.toString(),
            customer_name = config.customerName ?: "",
            price_code = config.priceCode.toString(),
            price_name = config.priceName ?: "",
            rnc        = config.rnc ?: ""
        )
    }

}






