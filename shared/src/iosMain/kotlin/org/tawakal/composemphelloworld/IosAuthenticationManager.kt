package org.tawakal.composemphelloworld

import cocoapods.MSAL.MSALAccount
import cocoapods.MSAL.MSALB2CAuthority
import cocoapods.MSAL.MSALInteractiveTokenParameters
import cocoapods.MSAL.MSALParameters
import cocoapods.MSAL.MSALPublicClientApplication
import cocoapods.MSAL.MSALPublicClientApplicationConfig
import cocoapods.MSAL.MSALSilentTokenParameters
import cocoapods.MSAL.MSALWebviewParameters
import cocoapods.MSAL.MSALWebviewType
import cocoapods.MSAL.getCurrentAccountWithParameters
import cocoapods.MSAL.homeAccountId
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.tawakal.composemphelloworld.msal.AuthenticationManager
import org.tawakal.composemphelloworld.msal.B2CConfiguration
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_MSAL_ACCESSTOKEN_KEY
import org.tawakal.composemphelloworld.utils.DataStoreManager
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.resume


@OptIn(ExperimentalForeignApi::class)
class IosAuthenticationManager : AuthenticationManager, KoinComponent {

    private val dataStoreManager: DataStoreManager by inject()
    private val scope: CoroutineScope by inject()

    private var accessToken: String? = null


    val kClientID = B2CConfiguration.scopes.first()
    val kAuthorityHostName = B2CConfiguration.azureAdB2CHostName
    val kTenantName = B2CConfiguration.tenantName
    val kSignupOrSigninPolicy = "B2C_1A_SIGNUP_SIGNIN"
    val kScopes =
        arrayOf("https://8tawakal.onmicrosoft.com/305ee585-489b-406b-b79c-95a5e1a45b26/User.Profile")


    private var application: MSALPublicClientApplication? = null

    private var webViewParameters: MSALWebviewParameters? = null

    private var savedAccount: MSALAccount? = null

    private val errorMessage = "No rootViewController found."


    @OptIn(BetaInteropApi::class)
    suspend fun signOutB2C(): AuthResult = suspendCancellableCoroutine { continuation ->
        try {
            // Ensure the MSAL application is initialized
            if (AppUser.shared.application == null) {
                // Call initializeMSAL() within a coroutine context
                val initResult = runBlocking { initializeMSAL() }
                if (initResult is AuthResult.Failure) {
                    println("Failed to initialize MSAL: ${initResult.error}")
                    continuation.resume(initResult) // Resume with the failure
                    return@suspendCancellableCoroutine
                }
            }
            val errorPtr = nativeHeap.alloc<ObjCObjectVar<NSError?>>()

            // Use MSAL's getCurrentAccount method to get the active account
            AppUser.shared.application?.getCurrentAccountWithParameters(null) { currentAccount, _, error ->
                if (error != null) {
                    // Handle error while retrieving the current account
                    continuation.resume(AuthResult.Failure("Error retrieving current account: ${error.localizedDescription}"))
                    return@getCurrentAccountWithParameters
                }

                // Check if the current account exists
                if (currentAccount == null) {
                    continuation.resume(AuthResult.Failure("No account found for sign out"))
                    return@getCurrentAccountWithParameters
                }

                // Successfully retrieved the current account
                val thisAccount = currentAccount

                // Call removeAccount from MSAL and check for success or failure
                val success =
                    AppUser.shared.application?.removeAccount(thisAccount, errorPtr.ptr) ?: false

                if (!success) {
                    // Handle error during account removal
                    val signoutError = errorPtr.value
                    continuation.resume(AuthResult.Failure("Sign out error: ${signoutError?.localizedDescription ?: "Unknown error during sign out"}"))
                } else {
                    // Successfully removed account
                    updateCurrentAccount(null)
                    continuation.resume(AuthResult.Success(null)) // Indicate success
                }
            }
        } catch (e: Exception) {
            // Catch any exceptions during the sign-out process and resume the coroutine with failure
            continuation.resume(AuthResult.Failure("Sign out error: ${e.message}", e))
        }
    }

    suspend fun signInSignUp(): AuthResult {
        return try {
            // Ensure the application is initialized
            if (application == null) {
                // Initialize the application
                val initResult = initializeMSAL()
                if (initResult is AuthResult.Failure) {
                    println("Initialization failed: ${initResult.error}")
                    return initResult  // Return failure if initialization fails
                }
            }

            println("Starting signInSignUp process...")  // Debug print
            // Call loadCurrentAccount to check if there's an existing account
            val authResult = loadCurrentAccount()
            println("Loaded current account: $authResult")  // Debug print

            when (authResult) {
                is AuthResult.Success -> {
                    println("Account found, proceeding to acquire token silently.")  // Debug print
                    // If an account is found, you can proceed with acquiring the token silently
                    return acquireTokenSilent() // This should return an AuthResult
                }

                is AuthResult.Failure -> {
                    println("No account found or error occurred: ${authResult.error}")  // Debug print
                    // If no account is found or if there was an error, start the sign-in process
                    return acquireToken() // This should also return an AuthResult
                }
            }
        } catch (e: Throwable) {
            println("Error in signInSignUp: ${e.message}")
            AuthResult.Failure("Error during sign-in/sign-up: ${e.message}")
        }
    }

    fun initializeMSAL(): AuthResult {
        return try {
            // Get authorities for sign-in and edit-profile policies
            val signInPolicyAuthority = getAuthority(kSignupOrSigninPolicy)

            // Create MSAL configuration with client ID and authority
            val pcaConfig = MSALPublicClientApplicationConfig(
                clientId = kClientID, redirectUri = null, authority = signInPolicyAuthority
            )
            pcaConfig.knownAuthorities = listOf(signInPolicyAuthority)

            // Initialize MSALPublicClientApplication
            application = MSALPublicClientApplication(pcaConfig, null)
            AppUser.shared.application = application
            // Store the application in AppUser (assuming you have a shared AppUser object)
            dispatch_async(dispatch_get_main_queue()) {
                // Initialize WebView parameters
                val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
                    ?: throw IllegalStateException(errorMessage)
                webViewParameters =
                    MSALWebviewParameters(authPresentationViewController = viewController)
                webViewParameters?.setWebviewType(MSALWebviewType.MSALWebviewTypeDefault)
            }

            AuthResult.Success(null)
        } catch (e: Throwable) {
            println("Unable to create application... ${e.message}")
            AuthResult.Failure("Initialization failed: ${e.message}")
        }
    }

    suspend fun loadCurrentAccount(): AuthResult = suspendCancellableCoroutine { continuation ->

        // Ensure the application is initialized
        if (application == null) {
            // Call initializeMSAL() within a coroutine context
            val initResult = runBlocking { initializeMSAL() }
            if (initResult is AuthResult.Failure) {
                println("Failed to initialize MSAL: ${initResult.error}")
                continuation.resume(initResult) // Resume with the failure
                return@suspendCancellableCoroutine
            }
        }
        val msalParameters = MSALParameters()
        msalParameters.completionBlockQueue = dispatch_get_main_queue()

        application?.getCurrentAccountWithParameters(msalParameters) { currentAccount, _, error ->
            if (error != null) {
                continuation.resume(
                    AuthResult.Failure(
                        "Error retrieving account: ${error.localizedDescription}",
                        error.asThrowable()
                    )
                )
                return@getCurrentAccountWithParameters
            }

            if (currentAccount != null) {
                savedAccount = currentAccount
                val authResult = updateCurrentAccount(currentAccount)
                continuation.resume(authResult)

            } else {
                continuation.resume(AuthResult.Failure("No account found"))
            }
        }
    }

    suspend fun acquireToken(): AuthResult = suspendCancellableCoroutine { continuation ->
        dispatch_async(dispatch_get_main_queue()) {
            UIApplication.sharedApplication.keyWindow?.rootViewController
                ?: return@dispatch_async continuation.resume(
                    AuthResult.Failure(errorMessage)
                )

            try {
                val signInPolicyAuthority = getAuthority(kSignupOrSigninPolicy)
                val parameters = MSALInteractiveTokenParameters(
                    scopes = kScopes.toList(), webviewParameters = webViewParameters!!
                )
                parameters.promptType()
                parameters.authority = signInPolicyAuthority

                application?.acquireTokenWithParameters(parameters) { result, error ->
                    if (error != null) {
                        continuation.resume(AuthResult.Failure(error.localizedDescription))
                    } else if (result != null) {
                        savedAccount = result.account
                        println("Username ${result.account.username()}")
                        val authResult = updateCurrentAccount(result.account)
                        continuation.resume(authResult)

                        accessToken = result.accessToken

                        print(result.accessToken)
                    } else {
                        continuation.resume(AuthResult.Failure("Unknown error occurred"))
                    }
                }
            } catch (e: Exception) {
                continuation.resume(AuthResult.Failure(e.message ?: "Unknown exception"))
            }
        }
    }

    suspend fun acquireTokenSilent() = suspendCancellableCoroutine { continuation ->
        dispatch_async(dispatch_get_main_queue()) {

            val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
                ?: return@dispatch_async continuation.resume(
                    AuthResult.Failure(errorMessage)
                )

            try {

                val errorPtr = nativeHeap.alloc<ObjCObjectVar<NSError?>>()

                // Fetch all accounts synchronously
                val accounts = AppUser.shared.application?.allAccounts(errorPtr.ptr)
                if (accounts.isNullOrEmpty()) {
                    continuation.resume(AuthResult.Failure("No accounts found"))
                    return@dispatch_async
                }

                // Cast accounts to List<MSALAccount>
                val accountsList = accounts.filterIsInstance<MSALAccount>()
                if (accountsList.isEmpty()) {
                    continuation.resume(AuthResult.Failure("Failed to cast accounts"))
                    return@dispatch_async
                }


                // Get the account associated with the given policy
                val thisAccount =
                    getAccountByPolicy(accounts = accountsList, policy = kSignupOrSigninPolicy)
                if (thisAccount == null) {
                    continuation.resume(AuthResult.Failure("No account found for policy $kSignupOrSigninPolicy"))
                    return@dispatch_async
                }

                // Get the authority for the policy
                val authority = getAuthority(kSignupOrSigninPolicy)

                // Create Webview parameters
                webViewParameters =
                    MSALWebviewParameters(authPresentationViewController = viewController)
                webViewParameters?.setWebviewType(MSALWebviewType.MSALWebviewTypeDefault)

                // Silent token acquisition parameters
                val parameters = MSALSilentTokenParameters(
                    scopes = kScopes.toList(), account = thisAccount
                )
                parameters.authority = authority

                // Acquire token silently
                AppUser.shared.application?.acquireTokenSilentWithParameters(parameters) { result, silentError ->
                    if (silentError != null) {
                        continuation.resume(AuthResult.Failure("Silent MSAL Error: ${silentError.localizedDescription}"))
                    } else if (result != null) {
                        // Extract claims and create UserAccount object
                        val accountClaims = result.account.accountClaims

                        accessToken = result.accessToken

                        // Use helper functions to get the values from account claims
                        val username = result.account.username ?: "Unknown"
                        val accountId = getStringClaim(accountClaims, "email")
                        val mobile = getStringClaim(accountClaims, "mobile_phone")
                        val city = getStringClaim(accountClaims, "city")
                        val name = getStringClaim(accountClaims, "name")
                        val givenName = getStringClaim(accountClaims, "given_name")
                        val familyName = getStringClaim(accountClaims, "family_name")
                        val streetAddress = getStringClaim(accountClaims, "street_address")
                        val referralVal = getBooleanClaim(accountClaims, "referralVal")
                        val approve = getBooleanClaim(accountClaims, "approve")

                        // Create UserAccount object
                        val userAccount = UserAccount(
                            username = username,
                            accountId = accountId,
                            mobile = mobile,
                            city = city,
                            name = name,
                            givenName = givenName,
                            familyName = familyName,
                            streetAddress = streetAddress,
                            referralVal = referralVal,
                            approve = approve
                        )
                        // Resume the continuation with success
                        continuation.resume(AuthResult.Success(userAccount))
                    } else {
                        continuation.resume(AuthResult.Failure("Silent token acquisition failed"))
                    }
                }
            } catch (e: Exception) {
                continuation.resume(AuthResult.Failure(e.message ?: "Unknown exception", e))
            }
        }
    }

    fun updateCurrentAccount(account: MSALAccount?): AuthResult {
        return if (account != null) {
            // Extract account claims
            val accountClaims = account.accountClaims

            // Use helper functions to extract values from claims
            val username = account.username ?: "Unknown"
            val accountId = getStringClaim(accountClaims, "email")
            val mobile = getStringClaim(accountClaims, "mobile_phone")
            val city = getStringClaim(accountClaims, "city")
            val name = getStringClaim(accountClaims, "name")
            val givenName = getStringClaim(accountClaims, "given_name")
            val familyName = getStringClaim(accountClaims, "family_name")
            val streetAddress = getStringClaim(accountClaims, "street_address")
            val referralVal = getBooleanClaim(accountClaims, "referralVal")
            val approve = getBooleanClaim(accountClaims, "approve")

            // Create a UserAccount object using the extracted claims
            val userAccount = UserAccount(
                username = username,
                accountId = accountId,
                mobile = mobile,
                city = city,
                name = name,
                givenName = givenName,
                familyName = familyName,
                streetAddress = streetAddress,
                referralVal = referralVal,
                approve = approve
            )
            // Return success with the UserAccount
            AppUser.shared.isLoggedIn = true
            AuthResult.Success(userAccount)
        } else {
            // Return failure if no account is found
            AuthResult.Failure("No account found")
        }
    }

    fun NSError.asThrowable(): Throwable {
        return Throwable(this.localizedDescription)
    }

    fun getAuthority(policy: String): MSALB2CAuthority {
        // Construct the authority URL string
        val authorityUrlString = "https://$kAuthorityHostName/tfp/$kTenantName/$policy"

        // Convert the URL string to NSURL, and handle any errors
        val authorityURL = NSURL(string = authorityUrlString)

        // Return the MSALB2CAuthority object
        return MSALB2CAuthority(authorityURL, null)
    }

    fun getAccountByPolicy(accounts: List<MSALAccount>, policy: String): MSALAccount? {
        for (account in accounts) {
            // Retrieve the home account ID and object ID
            val homeAccountId = account.homeAccountId?.identifier ?: continue
            val objectId = homeAccountId.split("-").firstOrNull() ?: continue

            // Check if the object ID ends with the policy (case-insensitive)
            if (objectId.endsWith(policy.lowercase())) {
                return account
            }
        }
        return null
    }

    // Helper function to get a string value from the claims map with a default fallback
    private fun getStringClaim(
        claims: Map<Any?, *>?, key: String, defaultValue: String = "Unknown"
    ): String {
        return claims?.get(key)?.toString() ?: defaultValue
    }

    // Helper function to get a boolean value from the claims map with a default fallback
    private fun getBooleanClaim(
        claims: Map<Any?, *>?, key: String, defaultValue: Boolean = false
    ): Boolean {
        return claims?.get(key)?.toString()?.toBoolean() ?: defaultValue
    }

    override fun acquireTokenSilently() {
        scope.launch {
            accessToken?.let { dataStoreManager.saveData(DATASTORE_PREF_MSAL_ACCESSTOKEN_KEY, it) }
        }
    }


}