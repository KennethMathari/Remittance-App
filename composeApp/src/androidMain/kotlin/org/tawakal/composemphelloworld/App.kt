package org.tawakal.composemphelloworld

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.tawakal.composemphelloworld.ui.navigation.Navigation

@Composable
@Preview
fun App(
    activity: Activity
) {

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    MaterialTheme {

        Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }) {
            it
            Navigation(
                activity = activity,
                navHostController = navController,
                snackbarHostState = snackbarHostState
            )
        }

    }
}