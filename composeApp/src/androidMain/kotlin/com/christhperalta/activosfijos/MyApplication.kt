package com.christhperalta.activosfijos

import android.app.Application
import com.christhperalta.activosfijos.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import qrgenerator.AppContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContext.apply { set(applicationContext) }
        SoundContextProvider.set(applicationContext)
        startKoin {
            androidContext(this@MyApplication)
            modules(appModules)
        }
    }
}
