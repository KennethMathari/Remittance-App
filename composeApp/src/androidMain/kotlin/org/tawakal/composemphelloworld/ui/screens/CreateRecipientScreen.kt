package org.tawakal.composemphelloworld.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.tawakal.composemphelloworld.domain.model.createRecipient.CreateRecipientRequestDomain
import org.tawakal.composemphelloworld.model.createRecipient.RecipientPresentation
import org.tawakal.composemphelloworld.model.validaterecipient.ValidateRecipientDataPresentation
import org.tawakal.composemphelloworld.ui.components.LoadingScreen
import org.tawakal.composemphelloworld.ui.viewmodel.CreateRecipientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecipientScreen(
    validateRecipientDataPresentation: ValidateRecipientDataPresentation,
    modifier: Modifier = Modifier,
    createRecipientViewModel: CreateRecipientViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    navigateToPaymentFormScreen: (RecipientPresentation)-> Unit
) {
    val createRecipientState by createRecipientViewModel.createRecipientState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val firstName = validateRecipientDataPresentation.recipient.firstName
    val lastName = validateRecipientDataPresentation.recipient.lastName
    val country = validateRecipientDataPresentation.recipient.country
    val usageLocation = validateRecipientDataPresentation.recipient.usageLocation
    var email by remember { mutableStateOf("") }
    val phoneNumber = validateRecipientDataPresentation.recipient.phone


    var expandedRelationship by remember { mutableStateOf(false) }
    var selectedRelationshipText by remember { mutableStateOf("") }
    val relationshipList = createRecipientViewModel.relationshipList()

    Box(modifier = modifier.fillMaxSize()) {
        if (createRecipientState.isLoading) {
            LoadingScreen()
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(
                        top = WindowInsets.navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Provide Additional Details for $firstName $lastName",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = modifier.padding(5.dp)
                )


                OutlinedTextField(value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    placeholder = { Text("Enter Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                    )
                )

                ExposedDropdownMenuBox(expanded = expandedRelationship,
                    onExpandedChange = { expandedRelationship = !expandedRelationship }) {
                    OutlinedTextField(
                        value = selectedRelationshipText,
                        readOnly = true,
                        onValueChange = { selectedRelationshipText = it },
                        label = { Text("Relationship") },
                        placeholder = {
                            Text("Enter Relationship")
                        },
                        modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRelationship)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    ExposedDropdownMenu(expanded = expandedRelationship,
                        onDismissRequest = { expandedRelationship = false }) {
                        relationshipList.forEach { selectionOption ->
                            DropdownMenuItem(text = {
                                Column {
                                    HorizontalDivider(
                                        thickness = 2.dp
                                    )
                                    Text(
                                        text = selectionOption.relationshipTitle,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = "(${selectionOption.relationshipDescription})"
                                    )

                                }

                            }, onClick = {
                                selectedRelationshipText = selectionOption.relationshipTitle
                                expandedRelationship =
                                    false // close the menu after selecting an option
                            })
                        }
                    }
                }

                val allFieldsFilled =
                    firstName.isNotEmpty() && lastName.isNotEmpty() && country.isNotEmpty() && email.isNotEmpty() && phoneNumber.isNotEmpty() && selectedRelationshipText.isNotEmpty()

                val createRecipientRequestDomain = CreateRecipientRequestDomain(
                    country = country,
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                    phone = phoneNumber,
                    relationship = selectedRelationshipText,
                    usageLocation = usageLocation
                )

                Button(onClick = {
                    if (allFieldsFilled) {
                        Log.e("AddRecipientScreen1", createRecipientRequestDomain.toString())
                        createRecipientViewModel.createRecipient(createRecipientRequestDomain)
                    }

                }, modifier = modifier.padding(10.dp)) {
                    Text(
                        text = "Add Recipient", style = MaterialTheme.typography.titleMedium
                    )
                }
            }

        }
    }

    LaunchedEffect(createRecipientState) {
        if (createRecipientState.recipientPresentation != null) {
            navigateToPaymentFormScreen(createRecipientState.recipientPresentation!!)
        }

    }

    LaunchedEffect(createRecipientState.successMessage) {
        scope.launch {
            createRecipientState.successMessage?.let {
                snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(createRecipientState.errorMessage) {
        scope.launch {
            createRecipientState.errorMessage?.let {
                snackbarHostState.showSnackbar(it)
            }
        }
    }

}