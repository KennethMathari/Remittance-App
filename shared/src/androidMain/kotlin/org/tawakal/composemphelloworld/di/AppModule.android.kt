package org.tawakal.composemphelloworld.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.dsl.module
import org.tawakal.composemphelloworld.msal.AndroidAuthenticationManager
import org.tawakal.composemphelloworld.msal.AuthenticationManager
import org.tawakal.composemphelloworld.utils.AndroidCurrencyManager
import org.tawakal.composemphelloworld.utils.AndroidEncryptionManager
import org.tawakal.composemphelloworld.utils.AndroidHmac
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_FILENAME
import org.tawakal.composemphelloworld.utils.CurrencyManager
import org.tawakal.composemphelloworld.utils.EncryptionManager
import org.tawakal.composemphelloworld.utils.Hmac


actual val appModule = module {

    single<DataStore<Preferences>> {
        val context = get<Context>()
        PreferenceDataStoreFactory.createWithPath(produceFile = {
            context.filesDir.resolve(DATASTORE_PREF_FILENAME).absolutePath.toPath()
        })
    }

    single<Hmac> {
        AndroidHmac()
    }

    single<CurrencyManager> {
        AndroidCurrencyManager(
            dataStoreManager = get()
        )
    }

    single<AuthenticationManager> {
        AndroidAuthenticationManager(
            dataStoreManager = get(),
            azureAppConfigRepository = get(),
            context = get(),
            scope = get(),
            currencyManager = get()
        )
    }

    single<EncryptionManager> {
        AndroidEncryptionManager()
    }

}