package org.tawakal.composemphelloworld.domain.repository

import kotlinx.coroutines.flow.Flow
import org.tawakal.composemphelloworld.domain.NetworkResult
import org.tawakal.composemphelloworld.domain.model.createRecipient.CreateRecipientDataDomain
import org.tawakal.composemphelloworld.domain.model.createRecipient.CreateRecipientRequestDomain
import org.tawakal.composemphelloworld.domain.model.listRecipients.ListRecipientResponseDomain
import org.tawakal.composemphelloworld.domain.model.validateRecipient.ValidateRecipientResponseDomain

interface RecipientRepository {

    suspend fun validateRecipient(
        phoneNumber: String
    ): Flow<NetworkResult<ValidateRecipientResponseDomain>>

    suspend fun createRecipient(
        createRecipientRequest: CreateRecipientRequestDomain
    ): NetworkResult<CreateRecipientDataDomain>

    suspend fun getRecipients(): Flow<NetworkResult<ListRecipientResponseDomain>>

}