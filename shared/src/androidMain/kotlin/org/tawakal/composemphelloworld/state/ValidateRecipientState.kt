package org.tawakal.composemphelloworld.state

import org.tawakal.composemphelloworld.model.validaterecipient.ValidateRecipientResponsePresentation


data class ValidateRecipientState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRecipientSaved: Boolean = false,
    val recipient: ValidateRecipientResponsePresentation? = null
)
