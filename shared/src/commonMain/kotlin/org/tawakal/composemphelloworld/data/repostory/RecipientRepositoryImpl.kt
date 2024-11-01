package org.tawakal.composemphelloworld.data.repostory

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import org.tawakal.composemphelloworld.data.mapper.toCreateRecipientDataDomain
import org.tawakal.composemphelloworld.data.mapper.toCreateRecipientRequestDTO
import org.tawakal.composemphelloworld.data.mapper.toListRecipientResponseDomain
import org.tawakal.composemphelloworld.data.mapper.toValidateRecipientResponseDomain
import org.tawakal.composemphelloworld.data.model.createRecipient.CreateRecipientResponseDTO
import org.tawakal.composemphelloworld.data.model.listRecipient.ListRecipientResponseDTO
import org.tawakal.composemphelloworld.data.model.validateRecipient.ValidateRecipientResponseDTO
import org.tawakal.composemphelloworld.data.utils.AuthRetry
import org.tawakal.composemphelloworld.domain.NetworkResult
import org.tawakal.composemphelloworld.domain.model.createRecipient.CreateRecipientDataDomain
import org.tawakal.composemphelloworld.domain.model.createRecipient.CreateRecipientRequestDomain
import org.tawakal.composemphelloworld.domain.model.listRecipients.ListRecipientResponseDomain
import org.tawakal.composemphelloworld.domain.model.validateRecipient.ValidateRecipientResponseDomain
import org.tawakal.composemphelloworld.domain.repository.RecipientRepository
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_CUSTOMERAPI_KEY
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_MSAL_ACCESSTOKEN_KEY
import org.tawakal.composemphelloworld.utils.DataStoreManager

class RecipientRepositoryImpl(
    private val httpClient: HttpClient,
    private val ioDispatcher: CoroutineDispatcher,
    private val dataStoreManager: DataStoreManager,
    private val authRetry: AuthRetry
) : RecipientRepository {

    private suspend fun getAccessTokenAndApiUrl(): Pair<String, String> {
        val msalAccessToken = dataStoreManager.getData(DATASTORE_PREF_MSAL_ACCESSTOKEN_KEY)
        val customerApiUrl = dataStoreManager.getData(DATASTORE_PREF_CUSTOMERAPI_KEY)
        return msalAccessToken to customerApiUrl
    }

    override suspend fun validateRecipient(
        phoneNumber: String
    ): Flow<NetworkResult<ValidateRecipientResponseDomain>> {

        val (msalAccessToken, customerApiUrl) = getAccessTokenAndApiUrl()

        return authRetry.executeWithAuthRetry(msalAccessToken = msalAccessToken,
            request = { token ->
                httpClient.get("$customerApiUrl/api/v1/recipient/externaluser/$phoneNumber") {
                    header("Authorization", "Bearer $token")
                }
            },
            processResponse = { response ->
                val validateRecipientResponseDomain =
                    response.body<List<ValidateRecipientResponseDTO>>().map {
                        it.toValidateRecipientResponseDomain()
                    }.first()
                NetworkResult.Success(validateRecipientResponseDomain)
            }).flowOn(ioDispatcher)
    }

    override suspend fun createRecipient(
        createRecipientRequest: CreateRecipientRequestDomain
    ): NetworkResult<CreateRecipientDataDomain> {

        val (msalAccessToken, customerApiUrl) = getAccessTokenAndApiUrl()

        return authRetry.executeWithAuthRetry(msalAccessToken = msalAccessToken,
            request = { token ->
                httpClient.post("$customerApiUrl/api/v1/recipient/create") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(createRecipientRequest.toCreateRecipientRequestDTO())
                }
            },
            processResponse = { response ->
                val createRecipientResponseDomain =
                    response.body<List<CreateRecipientResponseDTO>>().map {
                        it.body.data.toCreateRecipientDataDomain()
                    }.first()
                NetworkResult.Success(createRecipientResponseDomain)
            }).flowOn(ioDispatcher).first()
    }

    override suspend fun getRecipients(): Flow<NetworkResult<ListRecipientResponseDomain>> {

        val (msalAccessToken, customerApiUrl) = getAccessTokenAndApiUrl()

        return authRetry.executeWithAuthRetry(msalAccessToken = msalAccessToken,
            request = { token ->
                httpClient.get("$customerApiUrl/api/v1/recipient/list") {
                    header("Authorization", "Bearer $token")
                }
            },
            processResponse = { response ->
                val listRecipientResponseDomain =
                    response.body<ListRecipientResponseDTO>().toListRecipientResponseDomain()
                NetworkResult.Success(listRecipientResponseDomain)
            }).flowOn(ioDispatcher)

    }

}

