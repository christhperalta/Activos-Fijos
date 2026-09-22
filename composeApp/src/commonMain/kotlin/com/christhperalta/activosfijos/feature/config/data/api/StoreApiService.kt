package com.christhperalta.activosfijos.feature.config.data.api

import com.christhperalta.activosfijos.core.shared.SessionRepository
import com.christhperalta.activosfijos.core.utils.SapErrorResponse
import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.feature.config.data.api.customerDto.CustomerDto
import com.christhperalta.activosfijos.feature.config.data.api.priceListDto.PriceListDto
import com.christhperalta.activosfijos.feature.config.data.api.sellerDto.SellerDto
import com.christhperalta.activosfijos.feature.config.data.api.wareHouseDto.WareHouseDto
import com.christhperalta.activosfijos.feature.config.domain.repository.ServerConfigRepository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.isSuccess


class StoreApiService (
    private val client: HttpClient,
    private val serverConfigRepository: ServerConfigRepository,
    private val sessionRepository: SessionRepository,
) {
    private suspend fun getBaseUrl(): String {
        val config = serverConfigRepository.getServerConfig()
            ?: throw IllegalStateException("No hay configuración de servidor guardada")
        return "${config.protocol}://${config.host}:${config.port}"
    }

    suspend fun getSeller () : SellerDto {
        val baseUrl = getBaseUrl()
        val sessionId = sessionRepository.getSession()
        val response = client.get("$baseUrl/b1s/v1/view.svc/B1POS_VendorB1SLQuery"){
            header("Cookie", "B1SESSION=$sessionId")
        }

        if (!response.status.isSuccess()) {
            val errorBody = response.body<SapErrorResponse>()

            throw SapException(
                code = errorBody.error?.code,
                errorMessage = errorBody.error?.message?.value
            )
        }

       return  response.body<SellerDto>()
    }


    suspend fun getWarehouse () : WareHouseDto {
        val baseUrl = getBaseUrl()
        val sessionId = sessionRepository.getSession()
        val response = client.get("$baseUrl/b1s/v1/Warehouses?\$select=WarehouseName,WarehouseCode,Location&\$filter=Inactive eq 'tNO'"){
            header("Cookie", "B1SESSION=$sessionId")
        }

        if (!response.status.isSuccess()) {
            val errorBody = response.body<SapErrorResponse>()

            throw SapException(
                code = errorBody.error?.code,
                errorMessage = errorBody.error?.message?.value
            )
        }

        return  response.body<WareHouseDto>()
    }



    suspend fun getCustomers () : CustomerDto {
        val baseUrl = getBaseUrl()
        val sessionId = sessionRepository.getSession()
        val response = client.get("$baseUrl/b1s/v1/BusinessPartners?\$select=CardCode, CardName, Phone1, PayTermsGrpCode, FederalTaxID, CreditLimit,PriceListNum,U_NIT,CurrentAccountBalance,U_B1POS_Membre,MailAddress&\$filter= CardType  eq  'cCustomer'  and  U_B1POS  eq  'Y'"){
            header("Cookie", "B1SESSION=$sessionId")
        }

        if (!response.status.isSuccess()) {
            val errorBody = response.body<SapErrorResponse>()

            throw SapException(
                code = errorBody.error?.code,
                errorMessage = errorBody.error?.message?.value
            )
        }

        return  response.body<CustomerDto>()
    }




    suspend fun getPriceList () : PriceListDto {
        val baseUrl = getBaseUrl()
        val sessionId = sessionRepository.getSession()
        val response = client.get("$baseUrl/b1s/v1/PriceLists?\$select=PriceListName,PriceListNo"){
            header("Cookie", "B1SESSION=$sessionId")
        }

        if (!response.status.isSuccess()) {
            val errorBody = response.body<SapErrorResponse>()

            throw SapException(
                code = errorBody.error?.code,
                errorMessage = errorBody.error?.message?.value
            )
        }

        return  response.body<PriceListDto>()
    }







}