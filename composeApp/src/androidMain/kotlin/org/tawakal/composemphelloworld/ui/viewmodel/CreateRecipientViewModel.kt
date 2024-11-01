package org.tawakal.composemphelloworld.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tawakal.composemphelloworld.data.model.serviceData.Relationship
import org.tawakal.composemphelloworld.domain.NetworkResult
import org.tawakal.composemphelloworld.domain.model.createRecipient.CreateRecipientRequestDomain
import org.tawakal.composemphelloworld.domain.repository.RecipientRepository
import org.tawakal.composemphelloworld.mapper.toRecipientPresentation
import org.tawakal.composemphelloworld.msal.AndroidAuthenticationManager
import org.tawakal.composemphelloworld.state.CreateRecipientState
import org.tawakal.composemphelloworld.utils.ServiceData.serviceData

class CreateRecipientViewModel(
    private val recipientRepository: RecipientRepository,
    androidAuthenticationManager: AndroidAuthenticationManager
) : ViewModel() {

    private val _createRecipientState = MutableStateFlow(CreateRecipientState())
    val createRecipientState: StateFlow<CreateRecipientState> get() = _createRecipientState.asStateFlow()

    init {
        androidAuthenticationManager.acquireTokenSilently()
    }

    fun relationshipList(): List<Relationship> {
        return serviceData.relationship
    }

    fun createRecipient(createRecipientRequestDomain: CreateRecipientRequestDomain) {
        viewModelScope.launch {

            Log.e("CreateRecipientRequest", createRecipientRequestDomain.toString())

            _createRecipientState.value = CreateRecipientState(
                isLoading = true, errorMessage = null, successMessage = null
            )

            val result = recipientRepository.createRecipient(
                createRecipientRequestDomain
            )

            when (result) {
                is NetworkResult.ClientError -> {
                    updateErrorMessage("Unable to Add Recipient! Please Try Again.")
                }

                is NetworkResult.NetworkError -> {
                    updateErrorMessage("Unable to Add Recipient! Check Internet Connection.")
                }

                is NetworkResult.ServerError -> {
                    updateErrorMessage("Oops! Our Server is Down.")
                }

                is NetworkResult.Success -> {
                    val successMessage = "Recipient Added Successfully!"
                    _createRecipientState.value = CreateRecipientState(
                        successMessage = successMessage,
                        errorMessage = null,
                        isLoading = false,
                        recipientPresentation = result.data.recipient.toRecipientPresentation()
                    )
                }
            }
        }

    }

    private fun updateErrorMessage(errorMessage: String) {
        _createRecipientState.value = CreateRecipientState(
            successMessage = null,
            errorMessage = errorMessage,
            isLoading = false,
            recipientPresentation = null
        )
    }
}