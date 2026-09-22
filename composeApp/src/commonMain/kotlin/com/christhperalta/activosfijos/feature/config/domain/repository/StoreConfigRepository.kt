package com.christhperalta.activosfijos.feature.config.domain.repository


import com.christhperalta.activosfijos.feature.config.domain.model.Customer
import com.christhperalta.activosfijos.feature.config.domain.model.PriceList
import com.christhperalta.activosfijos.feature.config.domain.model.Seller
import com.christhperalta.activosfijos.feature.config.domain.model.StoreConfigModel
import com.christhperalta.activosfijos.feature.config.domain.model.Warehouse

interface StoreConfigRepository {
    suspend fun getStoreConfig(): StoreConfigModel?
    suspend fun saveStoreConfig(config: StoreConfigModel)
    suspend fun getSeller () : Result<List<Seller>>
    suspend fun getWarehouse () : Result<List<Warehouse>>
    suspend fun getCustomers () : Result<List<Customer>>
    suspend fun getPriceList () : Result<List<PriceList>>
}
