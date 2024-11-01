package org.tawakal.composemphelloworld.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.domain.model.createTransaction.CreateTransactionRequestDomain
import com.example.myapplication.domain.model.createTransaction.PaymentDomain
import com.example.myapplication.domain.model.createTransaction.RecipientDomain
import com.example.myapplication.domain.model.createTransaction.ServiceDomain
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.tawakal.composemphelloworld.data.model.serviceData.Service
import org.tawakal.composemphelloworld.data.model.serviceData.SubService
import org.tawakal.composemphelloworld.domain.model.getRate.GetRateRequestDomain
import org.tawakal.composemphelloworld.model.createRecipient.RecipientPresentation
import org.tawakal.composemphelloworld.ui.components.LoadingScreen
import org.tawakal.composemphelloworld.ui.viewmodel.PaymentFormViewModel
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_MSAL_SUB_KEY
import org.tawakal.composemphelloworld.utils.Constants.DATASTORE_PREF_USER_CURRENCYCODE_KEY
import org.tawakal.composemphelloworld.utils.CurrencyManager
import org.tawakal.composemphelloworld.utils.DataStoreManager
import org.tawakal.composemphelloworld.utils.TimeManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentFormScreen(
    recipientPresentation: RecipientPresentation,
    modifier: Modifier = Modifier,
    paymentFormViewModel: PaymentFormViewModel = koinViewModel(),
    timeManager: TimeManager = koinInject(),
    dataStoreManager: DataStoreManager = koinInject(),
    snackbarHostState: SnackbarHostState,
    currencyManager: CurrencyManager = koinInject()
) {
    val paymentFormState by paymentFormViewModel.paymentFormState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    var expandedPurpose by remember { mutableStateOf(false) }
    var selectedPurposeText by remember { mutableStateOf("") }
    var selectedPurposeCode by remember { mutableStateOf("") }
    val purposeOptions = paymentFormViewModel.getServiceData().purpose

    var expandedSourceOfIncome by remember { mutableStateOf(false) }
    var selectedSourceOfIncomeText by remember { mutableStateOf("") }
    val sourceOfIncomeOptions = paymentFormViewModel.getServiceData().sourceOfIncome

    var expandedPaymentMethod by remember { mutableStateOf(false) }
    var selectedPaymentMethodText by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf<Service?>(null) }
    val countryServiceOptions =
        paymentFormViewModel.getServiceData().countries.filter { it.countryCode == recipientPresentation.usageLocation }

    var expandedSubService by remember { mutableStateOf(false) }
    var selectedSubServiceText by remember { mutableStateOf("") }
    var selectedSubServiceCode by remember { mutableStateOf("") }
    var subServiceOptions by remember { mutableStateOf<List<SubService>>(emptyList()) }

    var expandedPickupLocation by remember { mutableStateOf(false) }
    var selectedPickupLocationText by remember { mutableStateOf("") }
    var selectedPickupLocationCode by remember { mutableStateOf("") }


    var sendingAmount by remember { mutableStateOf("") }

    val currentTimeStamp = timeManager.getCurrentTimestamp()

    val senderObjectId = runBlocking { dataStoreManager.getData(DATASTORE_PREF_MSAL_SUB_KEY) }
    val userCurrencyCode =
        runBlocking { dataStoreManager.getData(DATASTORE_PREF_USER_CURRENCYCODE_KEY) }

    val recipientCurrencyCode =
        runBlocking { currencyManager.getRecipientCurrencyCodeFromCountryCode(recipientPresentation.usageLocation.uppercase()) }


    Box(modifier = modifier.fillMaxSize()) {
        if (paymentFormState.isLoading) {
            LoadingScreen()
        } else {
            Column(
                modifier = modifier
                    .padding(
                        top = WindowInsets.navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                    )
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Make Payment to ${recipientPresentation.firstName}")

                //PaymentMethod Text Field
                ExposedDropdownMenuBox(expanded = expandedPaymentMethod, onExpandedChange = {
                    expandedPaymentMethod = !expandedPaymentMethod
                } // toggle expanded state
                ) {
                    OutlinedTextField(value = selectedPaymentMethodText,
                        readOnly = true,
                        onValueChange = {
                            selectedPaymentMethodText = it

                        },
                        label = { Text("Payment Method") },
                        placeholder = {
                            Text("Enter Payment Method")
                        },
                        modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPaymentMethod)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    ExposedDropdownMenu(expanded = expandedPaymentMethod,
                        onDismissRequest = { expandedPaymentMethod = false }) {

                        countryServiceOptions.get(0).service.forEach { selectionOption ->

                            DropdownMenuItem(text = { Text(selectionOption.title) }, onClick = {
                                selectedPaymentMethodText = selectionOption.title
                                selectedPaymentMethod = selectionOption
                                expandedPaymentMethod =
                                    false // close the menu after selecting an option
                                // Load subservice options based on selected payment method
                                subServiceOptions = selectionOption.subService
                                selectedSubServiceText = ""
                                selectedPickupLocationText = ""
                            })
                        }

                    }
                }

                // Display Subservice Dropdown if Payment Method is selected
                if (selectedPaymentMethod != null) {
                    ExposedDropdownMenuBox(expanded = expandedSubService,
                        onExpandedChange = { expandedSubService = !expandedSubService }) {
                        OutlinedTextField(
                            value = selectedSubServiceText,
                            readOnly = true,
                            onValueChange = { selectedSubServiceText = it },
                            label = { Text("Subservice") },
                            placeholder = { Text("Select Subservice") },
                            modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSubService)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors()
                        )

                        ExposedDropdownMenu(expanded = expandedSubService,
                            onDismissRequest = { expandedSubService = false }) {

                            subServiceOptions.forEach { subServiceOption ->
                                DropdownMenuItem(text = { Text(subServiceOption.serviceName) },
                                    onClick = {
                                        selectedSubServiceText = subServiceOption.serviceName
                                        selectedSubServiceCode = subServiceOption.serviceCode
                                        expandedSubService = false // Close dropdown after selecting
                                    })
                            }
                        }
                    }

                    if (selectedSubServiceText == "Cash") {
                        ExposedDropdownMenuBox(
                            expanded = expandedPickupLocation,
                            onExpandedChange = {
                                expandedPickupLocation = !expandedPickupLocation
                            }) {
                            OutlinedTextField(
                                value = selectedPickupLocationText,
                                readOnly = true,
                                onValueChange = { selectedPickupLocationText = it },
                                label = { Text("Pick-up Location") },
                                placeholder = { Text("Select Pickup Location") },
                                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPickupLocation)
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors()
                            )

                            ExposedDropdownMenu(expanded = expandedPickupLocation,
                                onDismissRequest = { expandedPickupLocation = false }) {

                                countryServiceOptions.get(0).city.forEach { pickupLocationOption ->
                                    DropdownMenuItem(text = { Text(pickupLocationOption.cityName) },
                                        onClick = {
                                            selectedPickupLocationText =
                                                pickupLocationOption.cityName
                                            selectedPickupLocationCode =
                                                pickupLocationOption.cityCode
                                            expandedPickupLocation = false
                                        })
                                }
                            }
                        }
                    }
                }


                ExposedDropdownMenuBox(expanded = expandedPurpose, onExpandedChange = {
                    expandedPurpose = !expandedPurpose
                } // toggle expanded state
                ) {
                    OutlinedTextField(
                        value = selectedPurposeText,
                        readOnly = true,
                        onValueChange = { selectedPurposeText = it },
                        label = { Text("Payment Purpose") },
                        placeholder = {
                            Text("Enter Payment Purpose")
                        },
                        modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPurpose)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    ExposedDropdownMenu(expanded = expandedPurpose,
                        onDismissRequest = { expandedPurpose = false }) {
                        purposeOptions.forEach { selectionOption ->
                            DropdownMenuItem(text = { Text(selectionOption.purposeDescription) },
                                onClick = {
                                    selectedPurposeText = selectionOption.purposeDescription
                                    selectedPurposeCode = selectionOption.purposeCode
                                    expandedPurpose =
                                        false // close the menu after selecting an option
                                })
                        }
                    }
                }

                ExposedDropdownMenuBox(expanded = expandedSourceOfIncome,
                    onExpandedChange = { expandedSourceOfIncome = !expandedSourceOfIncome }) {
                    OutlinedTextField(
                        value = selectedSourceOfIncomeText,
                        readOnly = true,
                        onValueChange = { selectedSourceOfIncomeText = it },
                        label = { Text("Source Of Income") },
                        placeholder = {
                            Text("Enter Source Of Income")
                        },
                        modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSourceOfIncome)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    ExposedDropdownMenu(expanded = expandedSourceOfIncome,
                        onDismissRequest = { expandedSourceOfIncome = false }) {
                        sourceOfIncomeOptions.forEach { selectionOption ->
                            DropdownMenuItem(text = { Text(selectionOption.sourceDescription) },
                                onClick = {
                                    selectedSourceOfIncomeText = selectionOption.sourceDescription
                                    expandedSourceOfIncome =
                                        false // close the menu after selecting an option
                                })
                        }
                    }
                }

                OutlinedTextField(
                    value = sendingAmount,
                    onValueChange = {
                        sendingAmount = it
                        val getRateRequestDomain = GetRateRequestDomain(
                            amount = sendingAmount,
                            curCode = "GBP",
                            receivingCountry = "KE",
                            sendingCountry = "GB",
                            service = "00014"
                        )

                        paymentFormViewModel.getRate(getRateRequestDomain)

                    },
                    label = {
                        Text(
                            text = "Amount to Send($userCurrencyCode)"
                        )
                    },
                    placeholder = {
                        Text("Enter Amount to Send($userCurrencyCode)")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    ),
                )

                OutlinedTextField(
                    value = paymentFormState.getRateResponseDomain?.data?.recipient?.payoutAmount.toString(),
                    onValueChange = {
                        paymentFormState.getRateResponseDomain?.data?.recipient?.payoutAmount =
                            it.toDouble()
                    },
                    label = {
                        Text(
                            text = "Amount to Receive($recipientCurrencyCode)"
                        )
                    },
                    placeholder = {
                        Text("Enter Amount to Receive($recipientCurrencyCode)")
                    },
                    readOnly = true,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    ),
                )

                val rateDomain = paymentFormState.getRateResponseDomain?.data?.recipient
                if (rateDomain != null) {
                    Text(text = "Payout Rate: ${rateDomain.payoutRate}")
                    Text(text = "Payout Currency: ${rateDomain.payoutCurrency}")
                    Text(text = "Payout Fee: ${rateDomain.payoutFee}")
                }

                val createTransactionRequestDomain = CreateTransactionRequestDomain(
                    timestamp = currentTimeStamp,
                    paymentMethod = selectedPaymentMethodText,
                    purpose = selectedPurposeCode,
                    senderObjectId = senderObjectId,
                    sourceOfIncomeCode = "104",
                    recipient = RecipientDomain(
                        firstName = recipientPresentation.firstName,
                        lastName = recipientPresentation.lastName,
                        mobilePhone = recipientPresentation.phone,
                        recipientObjectId = recipientPresentation.id,
                        relationshipCode = "005",
                        usageLocation = recipientPresentation.usageLocation
                    ),
                    service = ServiceDomain(
                        cityCode = selectedPickupLocationCode,
                        paymentMode = selectedSubServiceText,
                        serviceCode = selectedSubServiceCode,
                        serviceName = selectedPaymentMethodText,
                    ),
                    payment = PaymentDomain(
                        balance = 603.71,
                        fee = rateDomain?.payoutFee ?: 0.0,
                        rate = rateDomain?.payoutRate ?: 0.0,
                        recipientAmount = 627.21,
                        recipientCurrency = recipientCurrencyCode,
                        senderCurrency = userCurrencyCode
                    )
                )

                Button(
                    onClick = {
                        paymentFormViewModel.createTransaction(createTransactionRequestDomain)
                        Log.e("Transaction Request", createTransactionRequestDomain.toString())
                    }, modifier = modifier.padding(8.dp)
                ) {
                    Text(text = "Send Money")
                }

                LaunchedEffect(paymentFormState.errorMessage) {
                    scope.launch {
                        paymentFormState.errorMessage?.let {
                            snackbarHostState.showSnackbar(it)
                        }
                    }
                }

                LaunchedEffect(paymentFormState.successMessage) {
                    scope.launch {
                        paymentFormState.successMessage?.let {
                            snackbarHostState.showSnackbar(it)
                        }
                    }
                }
            }
        }
    }
}

