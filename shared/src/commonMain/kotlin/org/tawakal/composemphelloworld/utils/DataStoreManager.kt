package org.tawakal.composemphelloworld.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreManager(
    private val dataStore: DataStore<Preferences>, private val encryptionManager: EncryptionManager
) {
    suspend fun saveData(key: String, value: String) {
        val encryptedData = encryptionManager.encryptData(value)
        dataStore.edit { mutablePreferences ->
            mutablePreferences[byteArrayPreferencesKey(key)] = encryptedData
        }
    }

    suspend fun getData(key: String): String {

        val encryptedData = dataStore.data.map { preferences ->
            preferences[byteArrayPreferencesKey(key)]
        }.first()

        val decryptedData = encryptedData?.let { encryptionManager.decryptData(it) }

        return decryptedData ?: ""
    }
}