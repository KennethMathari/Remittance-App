package org.tawakal.composemphelloworld.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tawakal.composemphelloworld.domain.NetworkResult
import org.tawakal.composemphelloworld.domain.repository.RecipientRepository
import org.tawakal.composemphelloworld.model.validaterecipient.RecipientDataPresentation
import org.tawakal.composemphelloworld.model.validaterecipient.ValidateRecipientDataPresentation
import org.tawakal.composemphelloworld.model.validaterecipient.ValidateRecipientResponsePresentation
import org.tawakal.composemphelloworld.msal.AndroidAuthenticationManager
import org.tawakal.composemphelloworld.state.ValidateRecipientState

class ValidateRecipientViewModel(
    private val recipientRepository: RecipientRepository,
    androidAuthenticationManager: AndroidAuthenticationManager
) : ViewModel() {

    private val _validateRecipientState = MutableStateFlow(ValidateRecipientState())
    val validateRecipientState: StateFlow<ValidateRecipientState> get() = _validateRecipientState.asStateFlow()

    init {
        androidAuthenticationManager.acquireTokenSilently()
    }

    fun validateRecipient(phoneNumber: String, countryCode: String, country: String) {
        viewModelScope.launch {

            _validateRecipientState.value = ValidateRecipientState(
                isLoading = true, errorMessage = null, isRecipientSaved = false, recipient = null
            )

            recipientRepository.validateRecipient(phoneNumber).collect { result ->
                when (result) {
                    is NetworkResult.ClientError -> {
                        updateErrorMessage("Unable to Verify Recipient! Please Try Again!")
                    }

                    is NetworkResult.NetworkError -> {
                        updateErrorMessage("Unable to Verify Recipient! Check Internet Connection")
                    }

                    is NetworkResult.ServerError -> {
                        updateErrorMessage("Oops! Our Server is Down.")
                    }

                    is NetworkResult.Success -> {
                        if (result.data.source == "B2C") {
                            //Navigate to RecipientList Screen
                            _validateRecipientState.value = ValidateRecipientState(
                                isLoading = false,
                                errorMessage = "Recipient Already Exists!",
                                isRecipientSaved = true,
                                recipient = null
                            )
                            Log.e("B2C", "B2C")
                        } else if (result.data.source == "MMT") {
                            //Navigate to AddRecipient Screen

                            Log.e("MMT DATA", result.data.toString())
                            _validateRecipientState.value = ValidateRecipientState(
                                isLoading = false,
                                errorMessage = null,
                                isRecipientSaved = false,
                                recipient = ValidateRecipientResponsePresentation(
                                    validateRecipientData = ValidateRecipientDataPresentation(
                                        recipient = RecipientDataPresentation(
                                                country = country,
                                                displayName = result.data.validateRecipientData.recipient.displayName,
                                                firstName = result.data.validateRecipientData.recipient.firstName,
                                                lastName = result.data.validateRecipientData.recipient.lastName,
                                                id = result.data.validateRecipientData.recipient.id,
                                                phone = phoneNumber,
                                                usageLocation = countryCode
                                            )
                                    ),
                                    source = result.data.source,
                                    status = result.data.status,
                                    statusCode = result.data.statusCode,
                                    timeStamp = result.data.timeStamp
                                )
                            )
                            Log.e("MMT", "MMT")
                        }
                    }
                }

            }
        }
    }

    private fun updateErrorMessage(errorMessage: String) {
        _validateRecipientState.value = ValidateRecipientState(
            isLoading = false,
            errorMessage = errorMessage,
            isRecipientSaved = false,
            recipient = null
        )
    }
}