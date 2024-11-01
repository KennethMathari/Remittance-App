package org.tawakal.composemphelloworld

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.tawakal.composemphelloworld.di.androidModule
import org.tawakal.composemphelloworld.di.appModule
import org.tawakal.composemphelloworld.di.sharedModule

class TawakalPay: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TawakalPay)
            modules(appModule, androidModule, sharedModule)
        }
    }
}

