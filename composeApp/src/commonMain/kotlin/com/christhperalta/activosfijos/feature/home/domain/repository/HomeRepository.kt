package com.christhperalta.activosfijos.feature.home.domain.repository

import com.christhperalta.activosfijos.feature.home.domain.model.Quotations
import com.christhperalta.activosfijos.feature.home.domain.model.Rnc

interface HomeRepository {
    suspend fun quotation(quotationRequest: Quotations , endPoint : String): Result<String>
    suspend  fun validateRnc(rnc : String) : Result<Rnc>
}