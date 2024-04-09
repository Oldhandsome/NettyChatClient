package com.dtusystem.nettychatclient

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class NettyChatClientApp: Application() {
    init {
        //启用矢量图兼容
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            Timber.e(e)
        }
    }
}