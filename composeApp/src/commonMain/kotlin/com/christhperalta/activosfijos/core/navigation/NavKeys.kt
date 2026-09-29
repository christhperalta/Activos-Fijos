package com.christhperalta.activosfijos.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Login : NavKey

@Serializable
data object Config : NavKey

@Serializable
data object Home : NavKey

@Serializable
data object Onboarding : NavKey


@Serializable
data object Scanner : NavKey

@Serializable
data class AssetInformation(val assetCode: String) : NavKey

@Serializable
data class VerifyAsset(val assetCode: String) : NavKey

@Serializable
data class MaintenanceForm(val assetCode: String) : NavKey

@Serializable
data class PendingDecommissionForm(val assetCode: String) : NavKey

@Serializable
data class CountConfirmation(val assetCode: String) : NavKey

@Serializable
data object CountSummary : NavKey

@Serializable
data object ScannedAssets : NavKey

@Serializable
data class ScannedAssetDetail(val assetCode: String) : NavKey



@Serializable
data class Product(val productId: String? = null,val productBarCode : String? = null) : NavKey


@Serializable
data object Quotation : NavKey


@Serializable
data class Pdf (val pdfUrl: String) : NavKey

@Serializable
data object SearchProduct : NavKey
