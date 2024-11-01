package org.tawakal.composemphelloworld.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import org.koin.dsl.module
import org.tawakal.composemphelloworld.IosAuthenticationManager
import org.tawakal.composemphelloworld.msal.AuthenticationManager
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_FILENAME
import org.tawakal.composemphelloworld.utils.CurrencyManager
import org.tawakal.composemphelloworld.utils.Hmac
import org.tawakal.composemphelloworld.utils.IosCurrencyManager
import org.tawakal.composemphelloworld.utils.IosHmac
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val appModule = module {

    single<Hmac> {
        IosHmac()
    }

    single<CurrencyManager> {
        IosCurrencyManager(
            dataStoreManager = get()
        )
    }

    single<AuthenticationManager> {
        IosAuthenticationManager(
//            dataStoreManager = get(),
//            scope = get()
        )
    }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                val directory = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null
                )

                val filePath = requireNotNull(directory).path()+"/$DATASTORE_PREF_FILENAME"
                filePath.toPath()
            }
        )
    }

}