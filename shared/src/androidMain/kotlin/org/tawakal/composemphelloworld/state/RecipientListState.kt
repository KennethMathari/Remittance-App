package org.tawakal.composemphelloworld.state


import org.tawakal.composemphelloworld.domain.model.listRecipients.RecipientDomain

data class RecipientListState(
    val recipients: List<RecipientDomain>? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
