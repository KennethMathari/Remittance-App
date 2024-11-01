package org.tawakal.composemphelloworld.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tawakal.composemphelloworld.domain.NetworkResult
import org.tawakal.composemphelloworld.domain.repository.AzureAppConfigRepository
import org.tawakal.composemphelloworld.state.MainActivityState

class MainActivityViewModel(
    private val azureAppConfigRepository: AzureAppConfigRepository
) : ViewModel() {

    private val _mainActivityState = MutableStateFlow(MainActivityState())
    val mainActivityState: StateFlow<MainActivityState> = _mainActivityState

    init {
        fetchAzureAppConfigAccessToken()
    }

    private fun fetchAzureAppConfigAccessToken() {
        viewModelScope.launch {

            val result =
                azureAppConfigRepository.fetchAzureAppConfigAccessToken()

            when(result){
                is NetworkResult.ClientError -> {
                    updateErrorMessage("Unable to Fetch Azure Token! Please Open the App Again")
                }
                is NetworkResult.NetworkError -> {
                    updateErrorMessage("Unable to Fetch Azure Token! Check Internet Connection")
                }
                is NetworkResult.ServerError -> {
                    updateErrorMessage("Oops! Our Server is Down.")
                }
                is NetworkResult.Success -> {
                    _mainActivityState.value = MainActivityState(
                        azureAccessToken = result.data?.accessToken,
                        errorMessage = null
                    )
                }
            }
        }
    }

    private fun updateErrorMessage(errorMessage: String) {
        _mainActivityState.value = MainActivityState(
            azureAccessToken = null,
            errorMessage = errorMessage
        )
    }
}