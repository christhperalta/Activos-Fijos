package com.christhperalta.activosfijos.feature.home.domain.repository

import com.christhperalta.activosfijos.feature.home.domain.model.AssetCountSummary

interface HomeRepository {
    suspend fun getAssetCountSummary(): Result<AssetCountSummary>
}
