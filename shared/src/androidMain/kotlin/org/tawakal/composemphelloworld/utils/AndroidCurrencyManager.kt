package org.tawakal.composemphelloworld.utils

import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_USER_CURRENCYCODE_KEY
import java.util.Currency
import java.util.Locale

class AndroidCurrencyManager(
    private val dataStoreManager: DataStoreManager
) : CurrencyManager {

    override suspend fun saveUserCurrencyCodeFromCountryCode(countryCode: String) {
        val locale = Locale("", countryCode)
        val currency = Currency.getInstance(locale)
        currency.currencyCode

        dataStoreManager.saveData(DATASTORE_PREF_USER_CURRENCYCODE_KEY, currency.currencyCode)

    }

    override suspend fun getRecipientCurrencyCodeFromCountryCode(countryCode: String): String {
        val locale = Locale("", countryCode)
        val currency = Currency.getInstance(locale)
        return currency.currencyCode
    }
}