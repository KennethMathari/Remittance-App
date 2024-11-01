package org.tawakal.composemphelloworld.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.tawakal.composemphelloworld.msal.AndroidAuthenticationManager
import org.tawakal.composemphelloworld.ui.viewmodel.CreateRecipientViewModel
import org.tawakal.composemphelloworld.ui.viewmodel.MainActivityViewModel
import org.tawakal.composemphelloworld.ui.viewmodel.PaymentFormViewModel
import org.tawakal.composemphelloworld.ui.viewmodel.RecipientListViewModel
import org.tawakal.composemphelloworld.ui.viewmodel.ValidateRecipientViewModel


val androidModule = module {
    single<AndroidAuthenticationManager> {
        AndroidAuthenticationManager(
            dataStoreManager = get(),
            azureAppConfigRepository = get(),
            context = get(),
            scope = get(),
            currencyManager = get()
        )
    }


    viewModelOf(::RecipientListViewModel)
    viewModelOf(::MainActivityViewModel)
    viewModelOf(::ValidateRecipientViewModel)
    viewModelOf(::CreateRecipientViewModel)
    viewModelOf(::PaymentFormViewModel)
}