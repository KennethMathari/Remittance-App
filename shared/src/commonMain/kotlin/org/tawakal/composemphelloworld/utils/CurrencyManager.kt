package org.tawakal.composemphelloworld.utils

interface CurrencyManager {
    suspend fun saveUserCurrencyCodeFromCountryCode(countryCode: String)

    suspend fun getRecipientCurrencyCodeFromCountryCode(countryCode: String): String
}