package com.christhperalta.activosfijos.feature.config.data.repository

import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.feature.config.data.api.StoreApiService
import com.christhperalta.activosfijos.feature.config.data.api.customerDto.CustomerValue
import com.christhperalta.activosfijos.feature.config.data.api.priceListDto.PriceListValue
import com.christhperalta.activosfijos.feature.config.data.api.sellerDto.Value
import com.christhperalta.activosfijos.feature.config.data.api.wareHouseDto.WareHouseValue
import com.christhperalta.activosfijos.feature.config.data.local.ConfigDataSource
import com.christhperalta.activosfijos.feature.config.domain.model.Customer
import com.christhperalta.activosfijos.feature.config.domain.model.PriceList
import com.christhperalta.activosfijos.feature.config.domain.model.Seller
import com.christhperalta.activosfijos.feature.config.domain.model.StoreConfigModel
import com.christhperalta.activosfijos.feature.config.domain.model.Warehouse
import com.christhperalta.activosfijos.feature.config.domain.repository.StoreConfigRepository


class StoreConfigRepositoryImpl(
    private val dataSource: ConfigDataSource,
    private val storeApiService: StoreApiService
) : StoreConfigRepository {

    override suspend fun getStoreConfig(): StoreConfigModel? =
        dataSource.getStoreConfig()

    override suspend fun saveStoreConfig(config: StoreConfigModel) =
        dataSource.saveStoreConfig(config)

    override suspend fun getSeller(): Result<List<Seller>> {
        return try {
            val response = storeApiService.getSeller()
            Result.success(
                response.value.map{it.toDomain()}
            )

        } catch (e: SapException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWarehouse(): Result<List<Warehouse>> {
        return try {
            val response = storeApiService.getWarehouse()
            Result.success(
                response.value.map{it.toDomain()}
            )

        } catch (e: SapException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCustomers(): Result<List<Customer>> {
        return try {
            val response = storeApiService.getCustomers()
            Result.success(
                response.value.map{it.toDomain()}
            )

        } catch (e: SapException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPriceList(): Result<List<PriceList>> {
        return try {
            val response = storeApiService.getPriceList()
            Result.success(
                response.value.map{it.toDomain()}
            )

        } catch (e: SapException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}


fun Value.toDomain() : Seller {
    return Seller(

        slpCode = this.slpCode ?: 0,
        slpName = this.slpName ?: "",
    )
}


fun WareHouseValue.toDomain() : Warehouse {
    return Warehouse(

        warehouseCode = this.warehouseCode,
        warehouseName = this.warehouseName ?: "",
    )
}


fun CustomerValue.toDomain() : Customer {
    return Customer(
        customerCode = this.cardCode?: "",
        customerName = this.cardName ?: "",
    )
}

fun PriceListValue.toDomain() : PriceList {
    return PriceList(
        priceListCode = this.priceListNo.toString(),
        priceListName = this.priceListName ?: "",
    )
}

