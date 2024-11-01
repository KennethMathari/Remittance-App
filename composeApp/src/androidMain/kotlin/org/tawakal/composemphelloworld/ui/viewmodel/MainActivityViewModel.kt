package org.tawakal.composemphelloworld.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

            val azureAppConfigTokenResponseDTO =
                azureAppConfigRepository.fetchAzureAppConfigAccessToken()
            if (azureAppConfigTokenResponseDTO != null) {

                val accessToken = azureAppConfigTokenResponseDTO.accessToken
                _mainActivityState.value = MainActivityState(
                    accessToken = accessToken
                )
                println("Azure App Config Access Token: ${azureAppConfigTokenResponseDTO.accessToken}")

            } else {
                println("Failed to fetch access token from Azure App Config")
            }
        }
    }
}