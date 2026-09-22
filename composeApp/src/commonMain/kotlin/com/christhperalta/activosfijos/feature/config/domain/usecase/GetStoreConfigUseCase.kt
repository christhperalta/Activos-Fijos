package com.christhperalta.activosfijos.feature.config.domain.usecase

import com.christhperalta.activosfijos.feature.config.domain.model.StoreConfigModel
import com.christhperalta.activosfijos.feature.config.domain.repository.StoreConfigRepository

class GetStoreConfigUseCase (
    private val repository: StoreConfigRepository
)  {

     suspend operator fun invoke() : StoreConfigModel? {
        return repository.getStoreConfig()
    }
}