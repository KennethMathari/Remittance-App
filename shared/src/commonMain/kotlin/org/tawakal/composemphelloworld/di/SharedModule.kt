package org.tawakal.composemphelloworld.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.tawakal.composemphelloworld.data.repostory.AzureAppConfigRepositoryImpl
import org.tawakal.composemphelloworld.data.repostory.RecipientRepositoryImpl
import org.tawakal.composemphelloworld.data.repostory.TransactionsRepositoryImpl
import org.tawakal.composemphelloworld.data.utils.AuthRetry
import org.tawakal.composemphelloworld.domain.repository.AzureAppConfigRepository
import org.tawakal.composemphelloworld.domain.repository.RecipientRepository
import org.tawakal.composemphelloworld.domain.repository.TransactionsRepository
import org.tawakal.composemphelloworld.utils.DataStoreManager
import org.tawakal.composemphelloworld.utils.TimeManager

val sharedModule = module {

    single<DataStoreManager> {
        DataStoreManager(
            dataStore = get(),
            encryptionManager = get()
        )
    }

    single<TimeManager> {
        TimeManager()
    }

    single<AuthRetry> {
        AuthRetry(
            authenticationManager = get(),
            dataStoreManager = get()
        )
    }

    single<CoroutineDispatcher> {
        Dispatchers.IO
    }

    single<CoroutineScope> {
        val ioDispatcher = get<CoroutineDispatcher>()
        CoroutineScope(SupervisorJob() + ioDispatcher)
    }

    single<AzureAppConfigRepository> {
        AzureAppConfigRepositoryImpl(
            ioDispatcher = get(),
            httpClient = get(),
            hmac = get()
        )
    }

    single<RecipientRepository> {
        RecipientRepositoryImpl(
            httpClient = get(),
            ioDispatcher = get(),
            dataStoreManager = get(),
            authRetry = get()
        )
    }

    single<TransactionsRepository> {
        TransactionsRepositoryImpl(
            httpClient = get(),
            dataStoreManager = get(),
            authRetry = get()
        )
    }

    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
                exponentialDelay()
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 60_000
                connectTimeoutMillis = 60_000
                socketTimeoutMillis = 60_000
            }
        }
    }

}