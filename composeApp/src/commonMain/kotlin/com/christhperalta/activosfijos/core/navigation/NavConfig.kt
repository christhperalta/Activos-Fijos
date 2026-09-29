package com.christhperalta.activosfijos.core.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Login::class, Login.serializer())
            subclass(Home::class, Home.serializer())
            subclass(Config::class, Config.serializer())
            subclass(Onboarding::class, Onboarding.serializer())
            subclass(Scanner::class, Scanner.serializer())
            subclass(AssetInformation::class, AssetInformation.serializer())
            subclass(VerifyAsset::class, VerifyAsset.serializer())
            subclass(MaintenanceForm::class, MaintenanceForm.serializer())
            subclass(PendingDecommissionForm::class, PendingDecommissionForm.serializer())
            subclass(CountConfirmation::class, CountConfirmation.serializer())
            subclass(CountSummary::class, CountSummary.serializer())
            subclass(ScannedAssets::class, ScannedAssets.serializer())
            subclass(ScannedAssetDetail::class, ScannedAssetDetail.serializer())
            subclass(Product::class, Product.serializer())
            subclass(Quotation::class, Quotation.serializer())
            subclass(Pdf::class, Pdf.serializer())
            subclass(SearchProduct::class, SearchProduct.serializer())
        }
    }
}
