package org.tawakal.composemphelloworld.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.tawakal.composemphelloworld.msal.AndroidAuthenticationManager
import org.tawakal.composemphelloworld.state.UserDetails

@Composable
fun AuthScreen(
    androidAuthenticationManager: AndroidAuthenticationManager = koinInject(),
    activity: Activity,
    navigateToRecipientListScreen: (UserDetails) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {


    val authState by androidAuthenticationManager.authState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    if (authState.userDetails != null) {
        LaunchedEffect(authState.userDetails) {
            authState.userDetails?.let { navigateToRecipientListScreen(it) }
        }
    }


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(
                top = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateBottomPadding()
            )
            .fillMaxSize()
    ) {
        androidx.compose.material3.Button(onClick = {
            scope.launch {
                androidAuthenticationManager.fetchSecretClientId(activity)
            }
        }) {
            androidx.compose.material3.Text(text = "Sign In")
        }
    }

    LaunchedEffect(authState.errorMessage) {
        scope.launch {
            authState.errorMessage?.let {
                snackbarHostState.showSnackbar(it)
            }
        }
    }
}