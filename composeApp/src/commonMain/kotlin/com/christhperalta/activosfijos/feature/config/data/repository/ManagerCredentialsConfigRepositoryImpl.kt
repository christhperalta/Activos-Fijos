package com.christhperalta.activosfijos.feature.config.data.repository

import com.christhperalta.activosfijos.core.utils.CryptoUtil
import com.christhperalta.activosfijos.feature.config.data.local.ConfigDataSource
import com.christhperalta.activosfijos.feature.config.domain.model.ManagerCredentialConfigModel
import com.christhperalta.activosfijos.feature.config.domain.repository.ManagerCredentialsConfigRepository

class ManagerCredentialsConfigRepositoryImpl(
    private val dataSource: ConfigDataSource,
    private val cryptoUtil: CryptoUtil,
) : ManagerCredentialsConfigRepository {
    override suspend fun getManagerCredentialConfig(): ManagerCredentialConfigModel? =
        dataSource.getManagerCredentials()?.let {
            it.copy(password = cryptoUtil.decrypt(it.password))
        }

    override suspend fun saveManagerCredentialConfig(managerCredentials: ManagerCredentialConfigModel) =
        dataSource.saveMangerCredentials(
            managerCredentials.copy(
                password = cryptoUtil.encrypt(managerCredentials.password ?: "")
            )
        )
}