package org.tawakal.composemphelloworld.utils

import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_USER_CURRENCYCODE_KEY


class IosCurrencyManager(
    private val dataStoreManager: DataStoreManager
) : CurrencyManager {
    override suspend fun saveUserCurrencyCodeFromCountryCode(countryCode: String) {
        val currencyMap = mapOf(
            "US" to "USD",
            "CA" to "CAD",
            "GB" to "GBP",
            "KE" to "KES",
            // Add more countries and currencies here
        )

        currencyMap[countryCode.uppercase()]?.let {
            dataStoreManager.saveData(
                DATASTORE_PREF_USER_CURRENCYCODE_KEY,
                it
            )
        }
    }

    override suspend fun getRecipientCurrencyCodeFromCountryCode(countryCode: String): String {
        val currencyMap = mapOf(
            "US" to "USD",
            "CA" to "CAD",
            "GB" to "GBP",
            "KE" to "KES",
            // Add more countries and currencies here
        )

        return currencyMap[countryCode.uppercase()] ?: ""
    }
}