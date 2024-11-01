package org.tawakal.composemphelloworld.state

import org.tawakal.composemphelloworld.domain.model.getRate.GetRateResponseDomain


data class PaymentFormState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val getRateResponseDomain: GetRateResponseDomain? = null,
    val successMessage: String? = null,
)
