package org.tawakal.composemphelloworld.data.utils

import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.tawakal.composemphelloworld.domain.NetworkResult
import org.tawakal.composemphelloworld.msal.AuthenticationManager
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_MSAL_ACCESSTOKEN_KEY
import org.tawakal.composemphelloworld.utils.DataStoreManager

class AuthRetry(
    private val authenticationManager: AuthenticationManager,
    private val dataStoreManager: DataStoreManager,
) {

    // Generic function that handles request execution and retry in case of 401
    suspend fun <T> executeWithAuthRetry(
        msalAccessToken: String,
        request: suspend (String) -> HttpResponse,
        processResponse: suspend (HttpResponse) -> NetworkResult<T>
    ): Flow<NetworkResult<T>> {
        return flow {
            var token = msalAccessToken
            var retryAttempt = false

            var response = request.invoke(token)

            if (response.status.value == 401 && !retryAttempt) {
                retryAttempt = true
                token = refreshAccessToken()
                println("refresh Token")

                // Retry the request with the refreshed token
                response = request.invoke(token)
            }

            println(response.status)

            // Handle different response statuses
            when (response.status.value) {
                in 200..299 -> emit(processResponse(response)) // Success response
                400 -> emit(NetworkResult.ClientError(response.status.description))
                403 -> emit(NetworkResult.ClientError(response.status.description))
                404 -> emit(NetworkResult.ClientError(response.status.description))
                in 500..599 -> emit(NetworkResult.ServerError(response.status.description))
                else -> emit(NetworkResult.NetworkError(response.status.description))
            }

        }.catch { exception ->
            exception.printStackTrace()
            // Handle exceptions such as timeouts or connectivity issues
            when (exception) {
                is IOException -> emit(NetworkResult.NetworkError(exception.toString()))
                is TimeoutCancellationException -> emit(NetworkResult.NetworkError(exception.toString()))
                else -> {
                    exception.printStackTrace()
                    emit(NetworkResult.ClientError(exception.toString()))
                }
            }
        }
    }

    private suspend fun refreshAccessToken(): String {
        authenticationManager.acquireTokenSilently()
        return dataStoreManager.getData(DATASTORE_PREF_MSAL_ACCESSTOKEN_KEY)
    }
}