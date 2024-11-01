package org.tawakal.composemphelloworld.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.createTransaction.CreateTransactionRequestDomain
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tawakal.composemphelloworld.data.model.serviceData.ServiceResponse
import org.tawakal.composemphelloworld.domain.NetworkResult
import org.tawakal.composemphelloworld.domain.model.getRate.GetRateRequestDomain
import org.tawakal.composemphelloworld.domain.repository.TransactionsRepository
import org.tawakal.composemphelloworld.state.PaymentFormState
import org.tawakal.composemphelloworld.utils.ServiceData.serviceData

class PaymentFormViewModel(
    private val transactionsRepository: TransactionsRepository
) : ViewModel() {

    private val _paymentFormState = MutableStateFlow(PaymentFormState())
    val paymentFormState: StateFlow<PaymentFormState> get() = _paymentFormState.asStateFlow()

    private var getRateJob: Job? = null

    fun getRate(getRateRequestDomain: GetRateRequestDomain) {
        getRateJob?.cancel()
        getRateJob = viewModelScope.launch {

            when (val result = transactionsRepository.getRate(getRateRequestDomain)) {
                is NetworkResult.ClientError -> {
                    updateErrorMessage("Unable to Get Rate! Please Try Again.")
                }

                is NetworkResult.NetworkError -> {
                    updateErrorMessage("Unable to Get Rate! Check Internet Connection.")
                }

                is NetworkResult.ServerError -> {
                    updateErrorMessage("Oops! Our Server is Down!")
                }

                is NetworkResult.Success -> {
                    _paymentFormState.value = PaymentFormState(
                        getRateResponseDomain = result.data
                    )
                }
            }

        }
    }

    fun getServiceData(): ServiceResponse {
        return serviceData
    }

    fun createTransaction(createTransactionRequestDomain: CreateTransactionRequestDomain) {
        viewModelScope.launch {
            _paymentFormState.value = PaymentFormState(
                isLoading = true, errorMessage = null, getRateResponseDomain = null
            )

            val result = transactionsRepository.createTransaction(createTransactionRequestDomain)

            when (result) {
                is NetworkResult.ClientError -> {
                    updateErrorMessage("Unable to Send Money! Please Try Again.")
                }

                is NetworkResult.NetworkError -> {
                    updateErrorMessage("Unable to Send Money! Check Internet Connection.")
                }

                is NetworkResult.ServerError -> {
                    updateErrorMessage("Oops! Our Server is down.")
                }

                is NetworkResult.Success -> {
                    Log.e("Success","Transaction created successfully")
                    _paymentFormState.value = PaymentFormState(
                        isLoading = false,
                        errorMessage = null,
                        getRateResponseDomain = null,
                        successMessage = result.data
                    )
                }
            }

        }
    }

    private fun updateErrorMessage(errorMessage: String) {
        _paymentFormState.value = PaymentFormState(
            isLoading = false, errorMessage = errorMessage, getRateResponseDomain = null
        )
    }
}