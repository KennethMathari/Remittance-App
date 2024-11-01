package org.tawakal.composemphelloworld.utils

import org.koin.core.context.startKoin
import org.tawakal.composemphelloworld.di.appModule
import org.tawakal.composemphelloworld.di.sharedModule

class KoinHelper {
    fun initializeKoin() {
        startKoin {
            modules(sharedModule, appModule)
        }
    }
}
