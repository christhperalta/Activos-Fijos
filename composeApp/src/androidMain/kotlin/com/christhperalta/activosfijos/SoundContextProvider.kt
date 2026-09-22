package com.christhperalta.activosfijos

import android.content.Context

object SoundContextProvider {
    private lateinit var appContext: Context

    fun set(context: Context) {
        appContext = context.applicationContext
    }

    fun get(): Context = appContext
}