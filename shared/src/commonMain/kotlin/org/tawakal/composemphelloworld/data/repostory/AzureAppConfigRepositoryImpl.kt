package org.tawakal.composemphelloworld.data.repostory

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters
import io.ktor.http.Url
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import okio.ByteString
import okio.ByteString.Companion.decodeBase64
import okio.ByteString.Companion.encodeUtf8
import okio.ByteString.Companion.toByteString
import org.tawakal.composemphelloworld.data.model.azure.AzureAppConfigSecretResponseDTO
import org.tawakal.composemphelloworld.data.model.azure.AzureAppConfigTokenResponseDTO
import org.tawakal.composemphelloworld.data.model.azure.ConfigSettingDTO
import org.tawakal.composemphelloworld.domain.NetworkResult
import org.tawakal.composemphelloworld.domain.repository.AzureAppConfigRepository
import org.tawakal.composemphelloworld.utils.Constants.API_VERSION
import org.tawakal.composemphelloworld.utils.Constants.AZURE_APP_CONFIG_ACCESSTOKEN_URL
import org.tawakal.composemphelloworld.utils.Constants.AZURE_APP_CONFIG_BASE_URL
import org.tawakal.composemphelloworld.utils.Constants.AZURE_APP_CONFIG_SECRETS_BASE_URL
import org.tawakal.composemphelloworld.utils.Constants.CREDENTIAL
import org.tawakal.composemphelloworld.utils.Constants.GET_METHOD
import org.tawakal.composemphelloworld.utils.Constants.SECRET
import org.tawakal.composemphelloworld.utils.Constants.SIGNED_HEADERS
import org.tawakal.composemphelloworld.utils.Hmac

class AzureAppConfigRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val httpClient: HttpClient,
    private val hmac: Hmac
) : AzureAppConfigRepository {

    override suspend fun fetchAzureAppConfigAccessToken(): AzureAppConfigTokenResponseDTO? {
        return withContext(ioDispatcher) {
            try {

                val response = httpClient.post(AZURE_APP_CONFIG_ACCESSTOKEN_URL) {
                    setBody(FormDataContent(Parameters.build {
                        append("grant_type", "client_credentials")
                        append("client_id", "b47c95e2-1af2-4e0a-88e3-fd64ed9c5f69")
                        append("client_secret", "KJ08Q~tWQNX4Ctaq8-10sz_xmQxVAX88nwEvPahp")
                        append("scope", "https://vault.azure.net/.default")
                    }))
                }

                if (!response.status.isSuccess()) {
                    return@withContext null
                }

                val azureAppConfigTokenResponseDTO =
                    response.body<AzureAppConfigTokenResponseDTO?>()

                if (azureAppConfigTokenResponseDTO == null) {
                    println("Parsed data is null")
                }

                azureAppConfigTokenResponseDTO
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun fetchAzureAppConfigSecretValue(
        key: String, accessToken: String
    ): Flow<NetworkResult<AzureAppConfigSecretResponseDTO?>> {
        return flow {
            try {
                val url = "$AZURE_APP_CONFIG_SECRETS_BASE_URL/$key?api-version=7.3"
                val response = httpClient.get(url) {
                    header("Authorization", "Bearer $accessToken")
                }

                if (!response.status.isSuccess()) {
                    println("Error fetching secret configuration: ${response.status}")
                    emit(NetworkResult.NetworkError("${response.status}"))
                }

                val azureAppConfigSecretResponseDTO =
                    response.body<AzureAppConfigSecretResponseDTO?>()

                emit(NetworkResult.Success(azureAppConfigSecretResponseDTO))
            } catch (e: Exception) {
                println("Secret error $e")
                e.printStackTrace()
                emit(NetworkResult.ClientError(e.toString()))
            }
        }.flowOn(ioDispatcher)
    }

    override fun signRequest(host: String, path: String): Map<String, String> {
        val method = GET_METHOD

        val utcNow: String = Clock.System.now().toLocalDateTime(TimeZone.UTC).run {
            "${dayOfWeek.name.take(3)}, ${
                dayOfMonth.toString().padStart(2, '0')
            } " + "${month.name.take(3)} $year ${
                hour.toString().padStart(2, '0')
            }:" + "${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')} GMT"
        }

        val contentHash = ByteArray(0).toByteString().sha256().base64()


        val stringToSign = "$method\n$path\n$utcNow;$host;$contentHash"

        val secretKey = SECRET.decodeBase64()!!.toByteArray()

        val signatureBytes = hmac.hmacSHA256(secretKey, stringToSign.encodeUtf8().toByteArray())

        val encodedSignature = ByteString.of(*signatureBytes).base64()


        return mapOf(
            "x-ms-date" to utcNow,
            "x-ms-content-sha256" to contentHash,
            "Authorization" to "HMAC-SHA256 Credential=$CREDENTIAL&SignedHeaders=$SIGNED_HEADERS&Signature=$encodedSignature"
        )
    }

    override suspend fun fetchAzureAppConfigValue(
        key: String, label: String
    ): Flow<ConfigSettingDTO?> {
        return flow {
            try {
                val url = "$AZURE_APP_CONFIG_BASE_URL/$key?label=$label&api-version=$API_VERSION"
                val host = Url(url).host
                val headers = signRequest(
                    host = host, path = "/kv/$key?label=$label&api-version=$API_VERSION"
                )

                val response = httpClient.get(url) {
                    headers.forEach { (name, value) ->
                        header(name, value)
                    }
                }

                if (!response.status.isSuccess()) {
                    println("Error fetching configuration: ${response.status.description}")
                    emit(null)
                }

                emit(response.body<ConfigSettingDTO>())

            } catch (e: Exception) {
                e.printStackTrace()
                emit(null)
            }
        }.flowOn(ioDispatcher)
    }
}