package org.tawakal.composemphelloworld.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreManager(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveData(key: String, value: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[stringPreferencesKey(key)] = value
        }
    }

    suspend fun getData(key: String): String {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)]
        }.first() ?: ""
    }
}