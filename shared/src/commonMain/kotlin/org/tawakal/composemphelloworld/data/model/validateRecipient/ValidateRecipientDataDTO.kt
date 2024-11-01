package org.tawakal.composemphelloworld.data.model.validateRecipient


import com.example.myapplication.data.model.externaluser.RecipientDataDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ValidateRecipientDataDTO(
    @SerialName("Recipient")
    var recipient: RecipientDataDTO
)