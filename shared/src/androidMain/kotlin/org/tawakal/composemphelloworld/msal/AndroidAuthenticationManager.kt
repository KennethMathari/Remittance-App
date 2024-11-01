package org.tawakal.composemphelloworld.msal

import android.app.Activity
import android.content.Context
import android.util.Log
import org.tawakal.composemphelloworld.state.AuthState
import org.tawakal.composemphelloworld.state.UserDetails
import com.microsoft.identity.client.AcquireTokenParameters
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IMultipleAccountPublicClientApplication
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.Prompt
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.SilentAuthenticationCallback
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.tawakal.composemphelloworld.domain.NetworkResult
import org.tawakal.composemphelloworld.domain.repository.AzureAppConfigRepository
import org.tawakal.composemphelloworld.msal.B2CConfiguration.scopes
import org.tawakal.composemphelloworld.shared.R
import org.tawakal.composemphelloworld.utils.Constants.AZURE_APP_CONFIG_CUSTOMERAPI_KEY
import org.tawakal.composemphelloworld.utils.Constants.AZURE_APP_CONFIG_DEV_LABEL
import org.tawakal.composemphelloworld.utils.Constants.AZURE_APP_CONFIG_SECRET_CLIENTID_KEY
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_AZURE_ACCESSTOKEN_KEY
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_CUSTOMERAPI_KEY
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_MSAL_ACCESSTOKEN_KEY
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_MSAL_SUB_KEY
import org.tawakal.composemphelloworld.utils.CurrencyManager
import org.tawakal.composemphelloworld.utils.DataStoreManager

class AndroidAuthenticationManager(
    context: Context,
    private val dataStoreManager: DataStoreManager,
    private val azureAppConfigRepository: AzureAppConfigRepository,
    private val scope: CoroutineScope,
    private val currencyManager: CurrencyManager
) : AuthenticationManager {

    private lateinit var msalApplication: IMultipleAccountPublicClientApplication
    private var users: List<B2CUser>? = null

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> get() = _authState.asStateFlow()


    init {
        PublicClientApplication.createMultipleAccountPublicClientApplication(context.applicationContext,
            R.raw.auth_config,
            object : IPublicClientApplication.IMultipleAccountApplicationCreatedListener {
                override fun onCreated(application: IMultipleAccountPublicClientApplication) {
                    // Set the MultipleAccountPublicClientApplication to the class member b2cApp
                    msalApplication = application
                    // Load the account (if there is any)
                    loadAccounts()
                }

                override fun onError(exception: MsalException) {
                    exception.printStackTrace()
                }
            })
    }

    private fun loadAccounts() {
        msalApplication.getAccounts(object : IPublicClientApplication.LoadAccountsCallback {
            override fun onTaskCompleted(result: List<IAccount>) {
                users = B2CUser.getB2CUsersFromAccountList(result)
            }

            override fun onError(exception: MsalException) {
                exception.printStackTrace()
            }
        })
    }

    private val authInteractiveCallback: AuthenticationCallback
        get() {
            return object : AuthenticationCallback {
                override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                    scope.launch {

                        println("Successfully authenticated: $authenticationResult")

                        fetchCustomerApiFromAzureAppConfig(
                            AZURE_APP_CONFIG_CUSTOMERAPI_KEY, AZURE_APP_CONFIG_DEV_LABEL
                        )

                        val claims = authenticationResult?.account?.claims

                        Log.e("MSAL CLAIMS", claims.toString())

                        val senderObjectId = claims?.get("sub").toString()
                        val countryCode = claims?.get("usageLocation").toString()

                        saveMsalClaims(
                            senderObjectId, countryCode, authenticationResult?.accessToken ?: ""
                        )

                        val userDetails = UserDetails(
                            country = claims?.get("country").toString(),
                            unit = claims?.get("extension_unit").toString(),
                            email = claims?.get("email").toString(),
                            phoneNumber = claims?.get("mobile").toString(),
                            city = claims?.get("City").toString(),
                            streetAddress = claims?.get("streetAddress").toString(),
                            postalCode = claims?.get("PostNumber").toString(),
                            name = claims?.get("name").toString(),
                        )

                        _authState.value = AuthState(
                            userDetails = userDetails, errorMessage = null, scopes = null
                        )

                        /* Reload account asynchronously to get the up-to-date list. */
                        loadAccounts()
                    }
                }

                override fun onError(exception: MsalException?) {
                    exception?.printStackTrace()

                    when (exception) {
                        is MsalClientException -> {
                            Log.e("MSALClientERROR:", exception.toString())
                            _authState.value = AuthState(
                                userDetails = null,
                                errorMessage = "Unable to sign-in! Check Internet Connection.",
                                scopes = null
                            )
                        }

                        is MsalServiceException -> {
                            Log.e("MSALServiceERROR:", exception.toString())
                            _authState.value = AuthState(
                                userDetails = null,
                                errorMessage = "Unable to sign-in! Please try again.",
                                scopes = null
                            )
                        }
                    }
                }

                override fun onCancel() {/* User canceled the authentication */
                    println("User Cancelled Authentication Process!")
                }
            }
        }

    suspend fun fetchSecretClientId(activity: Activity) {
        val accessToken = runBlocking { dataStoreManager.getData(DATASTORE_PREF_AZURE_ACCESSTOKEN_KEY)}
        azureAppConfigRepository.fetchAzureAppConfigSecretValue(
            AZURE_APP_CONFIG_SECRET_CLIENTID_KEY, accessToken
        ).collect { result ->
            when (result) {
                is NetworkResult.ClientError -> {
                    updateErrorMessage("Unable to Login! Please try again")
                }

                is NetworkResult.NetworkError -> {
                    updateErrorMessage("Unable to Login! Check Internet Connection.")
                }

                is NetworkResult.ServerError -> {
                    updateErrorMessage("Oops! Our Server is down.")
                }

                is NetworkResult.Success -> {
                    val scopes = result.data?.value
                    if (scopes != null) {
                        signIn(activity, listOf(scopes))
                    }

                }
            }
        }
    }

    private fun updateErrorMessage(errorMessage: String) {
        _authState.value = AuthState(
            userDetails = null, errorMessage = errorMessage, scopes = null
        )
    }

    private fun signIn(activity: Activity, scopes: List<String>) {

        val parameters =
            AcquireTokenParameters.Builder().startAuthorizationFromActivity(activity).fromAuthority(
                B2CConfiguration.getAuthorityFromPolicyName(
                    B2CConfiguration.Policies[0]
                )
            ).withScopes(scopes).withPrompt(Prompt.LOGIN).withCallback(authInteractiveCallback)
                .build()

        msalApplication.acquireToken(parameters)
    }

    fun fetchCustomerApiFromAzureAppConfig(key: String, label: String) {
        runBlocking {
            azureAppConfigRepository.fetchAzureAppConfigValue(key, label).collect { customerApi ->
                if (customerApi != null) {
                    dataStoreManager.saveData(
                        DATASTORE_PREF_CUSTOMERAPI_KEY, customerApi.value
                    )
                    Log.e("CUSTOMER_API", customerApi.value)
                }
            }
        }
    }

    suspend fun saveMsalClaims(senderObjectId: String, countryCode: String, accessToken: String) {
        dataStoreManager.saveData(DATASTORE_PREF_MSAL_SUB_KEY, senderObjectId)
        currencyManager.saveUserCurrencyCodeFromCountryCode(countryCode)
        dataStoreManager.saveData(DATASTORE_PREF_MSAL_ACCESSTOKEN_KEY, accessToken)
    }

    override fun acquireTokenSilently() {
        runBlocking {
            Log.e("CallFunction:", "acquireTokenSilently()")
            val currentUser = users?.get(0)

            currentUser?.acquireTokenSilentAsync(multipleAccountPublicClientApplication = msalApplication,
                policyName = B2CConfiguration.Policies[0],
                scopes = scopes,
                callback = object : SilentAuthenticationCallback {
                    override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                        val accessToken = authenticationResult?.accessToken
                        accessToken?.let { Log.e("AccessTokenSilently", it) }
                        if (accessToken != null) {
                            scope.launch {
                                dataStoreManager.saveData(
                                    DATASTORE_PREF_MSAL_ACCESSTOKEN_KEY, accessToken
                                )
                            }
                        }
                    }

                    override fun onError(exception: MsalException?) {
                        exception?.printStackTrace()
                        //Redirect to auth screen
                        _authState.value = AuthState(
                            userDetails = null,
                            errorMessage = "Please Sign-In Again!",
                            scopes = null
                        )
                    }
                }

            )
        }
    }

}