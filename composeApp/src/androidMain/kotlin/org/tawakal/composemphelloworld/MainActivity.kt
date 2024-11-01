package org.tawakal.composemphelloworld

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.tawakal.composemphelloworld.ui.viewmodel.MainActivityViewModel
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_AZURE_ACCESSTOKEN_KEY
import org.tawakal.composemphelloworld.utils.DataStoreManager

class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by inject()
    private val dataStoreManager: DataStoreManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainActivityViewModel.mainActivityState.collect { mainActivityState ->
                    mainActivityState.accessToken?.let { accessToken ->
                        dataStoreManager.saveData(
                            DATASTORE_PREF_AZURE_ACCESSTOKEN_KEY, accessToken
                        )

                        Log.e("AccessTokenAZure", accessToken)
                    }
                }
            }
        }

        setContent {
            MaterialTheme {
                App(activity = this)
            }
        }
    }
}