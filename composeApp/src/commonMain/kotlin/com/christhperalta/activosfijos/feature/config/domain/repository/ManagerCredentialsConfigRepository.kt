package com.christhperalta.activosfijos.feature.config.domain.repository

import com.christhperalta.activosfijos.feature.config.domain.model.ManagerCredentialConfigModel

interface ManagerCredentialsConfigRepository {
    suspend fun getManagerCredentialConfig(): ManagerCredentialConfigModel?
    suspend fun saveManagerCredentialConfig(managerCredentials: ManagerCredentialConfigModel)
}