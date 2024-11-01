package org.tawakal.composemphelloworld.data.repostory


import com.example.myapplication.domain.model.createTransaction.CreateTransactionRequestDomain
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first
import org.tawakal.composemphelloworld.data.mapper.toCreateTransactionRequestDTO
import org.tawakal.composemphelloworld.data.mapper.toGetRateRequestDTO
import org.tawakal.composemphelloworld.data.mapper.toGetRateResponseDomain
import org.tawakal.composemphelloworld.data.model.getRate.GetRateResponseDTO
import org.tawakal.composemphelloworld.data.utils.AuthRetry
import org.tawakal.composemphelloworld.domain.NetworkResult
import org.tawakal.composemphelloworld.domain.model.getRate.GetRateRequestDomain
import org.tawakal.composemphelloworld.domain.model.getRate.GetRateResponseDomain
import org.tawakal.composemphelloworld.domain.repository.TransactionsRepository
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_CUSTOMERAPI_KEY
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_MSAL_ACCESSTOKEN_KEY
import org.tawakal.composemphelloworld.utils.DataStoreManager

class TransactionsRepositoryImpl(
    private val httpClient: HttpClient,
    private val dataStoreManager: DataStoreManager,
    private val authRetry: AuthRetry
) : TransactionsRepository {

    override suspend fun getRate(getRateRequestDomain: GetRateRequestDomain): NetworkResult<GetRateResponseDomain> {
        val msalAccessToken = dataStoreManager.getData(DATASTORE_PREF_MSAL_ACCESSTOKEN_KEY)
        val customerApiUrl = dataStoreManager.getData(DATASTORE_PREF_CUSTOMERAPI_KEY)

        return authRetry.executeWithAuthRetry(msalAccessToken = msalAccessToken,
            request = { token ->
                httpClient.post("$customerApiUrl/api/v1/recipient/rate") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(getRateRequestDomain.toGetRateRequestDTO())
                }
            },
            processResponse = { response ->
                val getRateResponseDomain =
                    response.body<GetRateResponseDTO>().toGetRateResponseDomain()
                NetworkResult.Success(getRateResponseDomain)
            }).first()
    }

    override suspend fun createTransaction(createTransactionRequestDomain: CreateTransactionRequestDomain): NetworkResult<String> {
        val msalAccessToken = dataStoreManager.getData(DATASTORE_PREF_MSAL_ACCESSTOKEN_KEY)
        val customerApiUrl = dataStoreManager.getData(DATASTORE_PREF_CUSTOMERAPI_KEY)

        return authRetry.executeWithAuthRetry(msalAccessToken = msalAccessToken,
            request = { token ->
                httpClient.post("$customerApiUrl/api/v1/recipient/createTransaction") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(createTransactionRequestDomain.toCreateTransactionRequestDTO())
                }
            },
            processResponse = {
                NetworkResult.Success("Success")
            }).first()
    }
}