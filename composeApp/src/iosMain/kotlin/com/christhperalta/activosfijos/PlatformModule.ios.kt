package com.christhperalta.activosfijos

import com.christhperalta.activosfijos.core.utils.CryptoUtil
import org.koin.dsl.module

actual val platformModule = module {
    single { DatabaseDriverFactory() }
    single { CryptoUtil() }
}