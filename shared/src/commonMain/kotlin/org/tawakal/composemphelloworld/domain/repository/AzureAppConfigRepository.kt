package org.tawakal.composemphelloworld.domain.repository

import kotlinx.coroutines.flow.Flow
import org.tawakal.composemphelloworld.data.model.azure.AzureAppConfigSecretResponseDTO
import org.tawakal.composemphelloworld.data.model.azure.AzureAppConfigTokenResponseDTO
import org.tawakal.composemphelloworld.data.model.azure.ConfigSettingDTO
import org.tawakal.composemphelloworld.domain.NetworkResult

interface AzureAppConfigRepository {

    suspend fun fetchAzureAppConfigAccessToken(): NetworkResult<AzureAppConfigTokenResponseDTO?>

    suspend fun fetchAzureAppConfigSecretValue(
        key: String, accessToken: String
    ): Flow<NetworkResult<AzureAppConfigSecretResponseDTO?>>

    fun signRequest(
        host: String, path: String
    ): Map<String, String>

    suspend fun fetchAzureAppConfigValue(key: String, label: String): Flow<ConfigSettingDTO?>
}