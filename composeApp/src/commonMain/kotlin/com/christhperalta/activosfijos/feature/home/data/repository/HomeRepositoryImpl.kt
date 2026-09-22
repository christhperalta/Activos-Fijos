package com.christhperalta.activosfijos.feature.home.data.repository

import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.feature.home.data.api.remote.HomeApiService
import com.christhperalta.activosfijos.feature.home.data.api.remote.quotationsDto.RncDto
import com.christhperalta.activosfijos.feature.home.domain.model.Quotations
import com.christhperalta.activosfijos.feature.home.domain.model.Rnc
import com.christhperalta.activosfijos.feature.home.domain.repository.HomeRepository


class HomeRepositoryImpl(
    private val apiService: HomeApiService
) : HomeRepository {
    override suspend fun quotation(quotationRequest: Quotations, endPoint: String): Result<String> {
        return try {
            val result = apiService.quotation(quotationRequest, endPoint)
            return Result.success(result)
        } catch (e: SapException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun validateRnc(rnc: String): Result<Rnc> {
        return try {
            val result = apiService.validateRNC(rnc = rnc)
            Result.success(result.toDomain())
        } catch (e: SapException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}


fun RncDto.toDomain(): Rnc {
    return Rnc(
        estatus = this.estatus,
        name = this.name,
        rnc = this.rnc
    )
}