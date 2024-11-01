package org.tawakal.composemphelloworld.domain

sealed interface NetworkResult<out D> {
    data class Success<out D>(val data: D) : NetworkResult<D>

    data class NetworkError(val error: String) : NetworkResult<Nothing>

    data class ClientError(val error: String) : NetworkResult<Nothing>

    data class ServerError(val error: String) : NetworkResult<Nothing>
}