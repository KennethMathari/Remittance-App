package org.tawakal.composemphelloworld.state

import org.tawakal.composemphelloworld.model.createRecipient.RecipientPresentation


data class CreateRecipientState(
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val recipientPresentation: RecipientPresentation? = null
)
