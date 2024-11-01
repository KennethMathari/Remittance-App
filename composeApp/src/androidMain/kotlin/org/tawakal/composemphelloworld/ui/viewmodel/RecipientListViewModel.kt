package org.tawakal.composemphelloworld.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.tawakal.composemphelloworld.domain.NetworkResult
import org.tawakal.composemphelloworld.domain.repository.RecipientRepository
import org.tawakal.composemphelloworld.state.RecipientListState

class RecipientListViewModel(
    private val recipientRepository: RecipientRepository,
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _recipientListState = MutableStateFlow(RecipientListState())
    val recipientListState: StateFlow<RecipientListState> get() = _recipientListState.asStateFlow()



    fun getRecipientsFromServer() {
        viewModelScope.launch {
            runBlocking {
                _recipientListState.value = RecipientListState(
                    recipients = null, isLoading = true, errorMessage = null
                )

                recipientRepository.getRecipients().collect { result ->
                    when (result) {
                        is NetworkResult.ClientError -> {
                            updateErrorMessage("Unable to Fetch Recipients!")
                        }

                        is NetworkResult.NetworkError -> {
                            updateErrorMessage("Check Internet Connection!")
                        }

                        is NetworkResult.ServerError -> {
                            updateErrorMessage("Oops! Our Server is Down!")
                        }

                        is NetworkResult.Success -> {
                            _recipientListState.value = RecipientListState(
                                recipients = result.data.data.recipients,
                                isLoading = false,
                                errorMessage = null
                            )

                            // Apply search filter
                            applySearchFilter()
                        }
                    }
                }
            }
        }
    }

    fun onSearchTextChanged(query: String) {
        _searchText.value = query
        applySearchFilter()
    }

    private fun applySearchFilter() {
        val query = _searchText.value.lowercase()
        val filteredRecipients = _recipientListState.value.recipients?.filter {
            it.firstName.lowercase().contains(query) || it.lastName.lowercase()
                .contains(query) || it.mobilePhone.contains(query)
        }
        _recipientListState.value = _recipientListState.value.copy(
            recipients = filteredRecipients
        )
    }


    private fun updateErrorMessage(errorMessage: String) {
        _recipientListState.value = RecipientListState(
            recipients = null, isLoading = false, errorMessage = errorMessage
        )
    }
}