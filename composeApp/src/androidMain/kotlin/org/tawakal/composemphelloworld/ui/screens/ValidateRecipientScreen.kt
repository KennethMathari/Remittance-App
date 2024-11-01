package org.tawakal.composemphelloworld.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joelkanyi.jcomposecountrycodepicker.component.CountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.tawakal.composemphelloworld.model.validaterecipient.RecipientDataPresentation
import org.tawakal.composemphelloworld.model.validaterecipient.ValidateRecipientDataPresentation
import org.tawakal.composemphelloworld.state.ValidateRecipientState
import org.tawakal.composemphelloworld.ui.components.LoadingScreen
import org.tawakal.composemphelloworld.ui.viewmodel.ValidateRecipientViewModel

@Composable
fun ValidateRecipientScreen(
    modifier: Modifier = Modifier,
    validateRecipientViewModel: ValidateRecipientViewModel = koinViewModel(),
    navigateToAddRecipientScreen: (ValidateRecipientDataPresentation) -> Unit,
    navigateToRecipientListScreen: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val validateRecipientState by validateRecipientViewModel.validateRecipientState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    HandleRecipientState(
        validateRecipientState = validateRecipientState,
        navigateToAddRecipientScreen = navigateToAddRecipientScreen,
        navigateToRecipientListScreen = navigateToRecipientListScreen
    )

    HandleSnackbarEffect(
        snackbarHostState = snackbarHostState,
        scope = scope,
        errorMessage = validateRecipientState.errorMessage
    )

    Box(modifier = modifier.fillMaxSize()) {
        if (validateRecipientState.isLoading) {
            LoadingScreen()
        } else {
            RecipientForm(
                modifier = modifier,
                onValidateRecipient = { fullPhoneNumber, countryCode, country ->
                    validateRecipientViewModel.validateRecipient(fullPhoneNumber, countryCode, country)
                },
                navigateToAddRecipientScreen = navigateToAddRecipientScreen
            )
        }
    }
}

@Composable
fun HandleRecipientState(
    validateRecipientState: ValidateRecipientState,
    navigateToAddRecipientScreen: (ValidateRecipientDataPresentation) -> Unit,
    navigateToRecipientListScreen: () -> Unit
) {
    LaunchedEffect(validateRecipientState) {
        if (validateRecipientState.isRecipientSaved) {
            navigateToRecipientListScreen()
        } else {
            validateRecipientState.recipient?.validateRecipientData?.let { validateRecipientDataPresentation ->
                navigateToAddRecipientScreen(validateRecipientDataPresentation)
            }
        }
    }
}

@Composable
fun HandleSnackbarEffect(
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    errorMessage: String?
) {
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Long
                )
            }
        }
    }
}

@Composable
fun RecipientForm(
    modifier: Modifier = Modifier,
    onValidateRecipient: (String, String, String) -> Unit,
    navigateToAddRecipientScreen: (ValidateRecipientDataPresentation) -> Unit
) {
    val phoneNumber = rememberSaveable { mutableStateOf("") }
    val state = rememberKomposeCountryCodePickerState()
    val isPhoneNumberValid = rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PhoneNumberField(
            phoneNumber = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            state = state,
            isPhoneNumberValid = isPhoneNumberValid.value
        )

        ConfirmButton(
            phoneNumber = phoneNumber.value,
            state = state,
            isPhoneNumberValid = isPhoneNumberValid.value,
            onValidateRecipient = { fullPhoneNumber, countryCode, country ->
                isPhoneNumberValid.value = state.isPhoneNumberValid(fullPhoneNumber)
                if (isPhoneNumberValid.value) {
                    if (countryCode == "SO" || countryCode == "KE") {
                        onValidateRecipient(fullPhoneNumber, countryCode, country)
                    } else {
                        navigateToAddRecipientScreen(
                            ValidateRecipientDataPresentation(
                                recipient = RecipientDataPresentation(
                                    country = country,
                                    displayName = "",
                                    firstName = "",
                                    id = "",
                                    lastName = "",
                                    phone = fullPhoneNumber,
                                    usageLocation = countryCode
                                )
                            )
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun PhoneNumberField(
    phoneNumber: String,
    onValueChange: (String) -> Unit,
    state: CountryCodePicker,
    isPhoneNumberValid: Boolean
) {
    KomposeCountryCodePicker(
        text = phoneNumber,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = "Enter Recipient's Phone Number", fontSize = 12.sp)
        },
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
        ),
        state = state,
        error = !isPhoneNumberValid
    )
}

@Composable
fun ConfirmButton(
    phoneNumber: String,
    state: CountryCodePicker,
    isPhoneNumberValid: Boolean,
    onValidateRecipient: (String, String, String) -> Unit
) {
    Button(
        onClick = {
            val fullPhoneNumber = state.getFullPhoneNumber()
            val countryCode = state.countryCode.uppercase()
            val country = state.getCountryName()

            if (phoneNumber.isNotEmpty()) {
                Log.e("PhoneNumber", fullPhoneNumber)
                Log.e("CountryCode", countryCode)
                Log.e("Country", country)
                Log.e("isPhoneValid", state.isPhoneNumberValid(fullPhoneNumber).toString())

                onValidateRecipient(fullPhoneNumber, countryCode, country)
            }
        },
        modifier = Modifier.padding(10.dp)
    ) {
        Text(text = "Confirm", style = MaterialTheme.typography.titleMedium)
    }
}

