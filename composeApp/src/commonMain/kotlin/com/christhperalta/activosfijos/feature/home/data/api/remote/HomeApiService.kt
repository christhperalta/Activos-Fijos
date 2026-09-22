package com.christhperalta.activosfijos.feature.home.data.api.remote

import com.christhperalta.activosfijos.core.constants.Constants
import com.christhperalta.activosfijos.core.quotation.Product
import com.christhperalta.activosfijos.core.shared.SessionRepository
import com.christhperalta.activosfijos.core.utils.SapErrorResponse
import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.feature.config.domain.repository.ServerConfigRepository
import com.christhperalta.activosfijos.feature.home.data.api.remote.quotationsDto.DocumentLine
import com.christhperalta.activosfijos.feature.home.data.api.remote.quotationsDto.QuotationsDto
import com.christhperalta.activosfijos.feature.home.data.api.remote.quotationsDto.RncDto
import com.christhperalta.activosfijos.feature.home.domain.model.Quotations
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


class HomeApiService (
    private val client: HttpClient,
    private val serverConfigRepository: ServerConfigRepository,
    private val sessionRepository: SessionRepository,
) {
    private suspend fun getBaseUrl(): String {
        val config = serverConfigRepository.getServerConfig()
            ?: throw IllegalStateException("No hay configuración de servidor guardada")
        return "${config.protocol}://${config.host}:${config.port}"
    }


    suspend fun validateRNC (rnc : String) : RncDto {
        val response = client.get("${Constants.RNC_VALIDATION_URL}?rnc=$rnc")

        if (!response.status.isSuccess()) {
            val errorBody = response.body<SapErrorResponse>()
            throw SapException(
                code = errorBody.error?.code,
                errorMessage = errorBody.error?.message?.value
            )
        }

        return response.body<RncDto>()
    }



    suspend fun quotation(quotationRequest: Quotations, endPoint : String) : String {
        val baseUrl = getBaseUrl()
        val sessionId = sessionRepository.getSession()
        val response = client.post("$baseUrl/b1s/v1/$endPoint") {
            contentType(ContentType.Application.Json)
            header("Cookie", "B1SESSION=$sessionId")
            setBody(
                QuotationsDto(
                    cardCode = quotationRequest.cardCode,
                    cardName = quotationRequest.cardName,
                    comments = quotationRequest.comments,
                    docCurrency = quotationRequest.docCurrency,
                    docDate = quotationRequest.docDate,
                    docDueDate = quotationRequest.docDueDate,
                    docObjectCode = quotationRequest.docObjectCode,
                    docType = quotationRequest.docType,
                    federalTaxID = quotationRequest.federalTaxID,
                    salesPersonCode = quotationRequest.salesPersonCode,
                    uB1POS = quotationRequest.uB1POS,
                    uB1POSCaja = quotationRequest.uB1POSCaja,
                    uB1POSItem = quotationRequest.uB1POSItem,
                    uB1POST = quotationRequest.uB1POST,
                    uB1POSU = quotationRequest.uB1POSU,
                    uFacFecha = quotationRequest.uFacFecha,
                    uFacNit = quotationRequest.uFacNit,
                    uFacNom = quotationRequest.uFacNom,
                    uFacSerie = quotationRequest.uFacSerie,
                    uTNegocio = quotationRequest.uTNegocio,
                    documentLines = quotationRequest.documentLines?.map{
                        it?.toDto()
                    }

                )
            )
        }
        if (!response.status.isSuccess()) {
            val errorBody = response.body<SapErrorResponse>()
            throw SapException(
                code = errorBody.error?.code,
                errorMessage = errorBody.error?.message?.value
            )
        }

        return  response.body<QuotationResponse>().docNum.toString()
    }

}

fun Product.toDto(): DocumentLine {
    return DocumentLine(
        itemCode = this.itemCode,
        itemDescription = this.itemDescription,
        quantity = this.quantity.toString(),
        baseLine = this.baseLine,
        taxCode = this.taxCode,
        unitPrice = this.unitPrice?.let { cents ->
            val integer = cents / 100
            val fraction = (cents % 100).toString().padStart(2, '0')
            "$integer.$fraction"
        }
    )
}


@Serializable
data class QuotationResponse(
    @SerialName("DocNum")
    val docNum: Int
)