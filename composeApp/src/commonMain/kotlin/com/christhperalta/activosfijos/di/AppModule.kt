package com.christhperalta.activosfijos.di

import com.christhperalta.activosfijos.AppViewModel
import com.christhperalta.activosfijos.core.quotation.QuotationCart
import com.christhperalta.activosfijos.DatabaseDriverFactory
import com.christhperalta.activosfijos.SoundPlayer
import com.christhperalta.activosfijos.core.shared.SessionRepository
import com.christhperalta.activosfijos.core.shared.UserNameRepository
import com.christhperalta.activosfijos.createHttpClient
import com.christhperalta.activosfijos.database.AppDatabase
import com.christhperalta.activosfijos.feature.config.data.api.StoreApiService
import com.christhperalta.activosfijos.feature.config.data.local.ConfigDataSource
import com.christhperalta.activosfijos.feature.config.data.repository.ManagerCredentialsConfigRepositoryImpl
import com.christhperalta.activosfijos.feature.config.data.repository.ServerConfigRepositoryImpl
import com.christhperalta.activosfijos.feature.config.data.repository.StoreConfigRepositoryImpl
import com.christhperalta.activosfijos.feature.config.domain.repository.ManagerCredentialsConfigRepository
import com.christhperalta.activosfijos.feature.config.domain.repository.ServerConfigRepository
import com.christhperalta.activosfijos.feature.config.domain.repository.StoreConfigRepository
import com.christhperalta.activosfijos.feature.config.domain.usecase.GetStoreConfigUseCase
import com.christhperalta.activosfijos.feature.config.presentation.ConfigViewModel
import com.christhperalta.activosfijos.feature.home.data.api.remote.HomeApiService
import com.christhperalta.activosfijos.feature.home.data.repository.HomeRepositoryImpl
import com.christhperalta.activosfijos.feature.home.domain.repository.HomeRepository
import com.christhperalta.activosfijos.feature.home.presentation.HomeViewModel
import com.christhperalta.activosfijos.feature.login.data.remote.api.PosApiService
import com.christhperalta.activosfijos.feature.login.data.repository.LoginRepositoryImpl
import com.christhperalta.activosfijos.feature.login.domain.repository.LoginRepository
import com.christhperalta.activosfijos.feature.login.domain.use_case.LogInUserUseCase
import com.christhperalta.activosfijos.feature.login.presentation.LoginViewModel
import com.christhperalta.activosfijos.feature.pdf.data.remote.api.PdfApiService
import com.christhperalta.activosfijos.feature.pdf.data.repository.PdfRepositoryImpl
import com.christhperalta.activosfijos.feature.pdf.domain.PdfRepository
import com.christhperalta.activosfijos.feature.pdf.presentation.PdfViewModel
import com.christhperalta.activosfijos.feature.product.data.remote.api.ProductApiService
import com.christhperalta.activosfijos.feature.product.data.repository.ProductRepositoryImpl
import com.christhperalta.activosfijos.feature.product.domain.repository.ProductRepository
import com.christhperalta.activosfijos.feature.product.presentation.ProductDetailViewModel
import com.christhperalta.activosfijos.feature.scanner.presentation.ScannerViewModel
import com.christhperalta.activosfijos.feature.inventory.data.FakeInventoryRepository
import com.christhperalta.activosfijos.feature.inventory.domain.InventoryRepository
import com.christhperalta.activosfijos.platformModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val databaseModule = module {
    single<AppDatabase> { AppDatabase(get<DatabaseDriverFactory>().createDriver()) }
    single { ConfigDataSource(get()) }
    single<ServerConfigRepository> { ServerConfigRepositoryImpl(get(), get()) }
    single<StoreConfigRepository> { StoreConfigRepositoryImpl(get(),get()) }
    single<ManagerCredentialsConfigRepository> { ManagerCredentialsConfigRepositoryImpl(get(), get()) }
    single<HomeRepository> { HomeRepositoryImpl(get()) }
}
val quotationModule = module {
    single { QuotationCart() }
}

val soundPlayerModule = module {
    factory { SoundPlayer() }
}

val networkModule = module {
    single { SessionRepository() }
    single { UserNameRepository() }
    single { createHttpClient() }
    single { PosApiService(get(), get(), get(),get()) }
    single { PdfApiService(get()) }
    single { HomeApiService(get(), get(), get()) }
    single { ProductApiService(get(),get(),get(),get()) }
    single { StoreApiService(get(),get(),get()) }

}

val repositoryModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    single<PdfRepository> { PdfRepositoryImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<InventoryRepository> { FakeInventoryRepository() }


}

val useCaseModule = module {
    factory { LogInUserUseCase(get()) }
    factory{ GetStoreConfigUseCase(get()) }
}


val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get(),get()) }
    viewModel { AppViewModel(get() )}
    viewModel { ConfigViewModel(get(), get(),get(),get()) }
    viewModel { ScannerViewModel(get()) }
    viewModel { PdfViewModel(get()) }
    viewModel { ProductDetailViewModel(get()) }


}


val appModules =
    listOf(
        platformModule,
        quotationModule,
        networkModule,
        repositoryModule,
        useCaseModule,
        viewModelModule,
        databaseModule,
        soundPlayerModule,
    )
