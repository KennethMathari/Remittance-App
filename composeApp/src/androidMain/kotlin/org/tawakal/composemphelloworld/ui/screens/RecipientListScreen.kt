package org.tawakal.composemphelloworld.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import org.tawakal.composemphelloworld.domain.model.listRecipients.RecipientDomain
import org.tawakal.composemphelloworld.model.createRecipient.RecipientPresentation
import org.tawakal.composemphelloworld.ui.viewmodel.RecipientListViewModel

@Composable
fun RecipientListScreen(
    modifier: Modifier = Modifier,
    recipientListViewModel: RecipientListViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    navigateToPaymentFormScreen: (RecipientPresentation) -> Unit,
    navigateToValidateRecipientScreen: () -> Unit,
    navigateToTransactionsDetailScreen:(RecipientDomain)->Unit
) {
    val recipientListState by recipientListViewModel.recipientListState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    var searchRecipientText by remember { mutableStateOf("") }

    recipientListViewModel.getRecipientsFromServer()

    LaunchedEffect(recipientListState.errorMessage) {
        scope.launch {
            recipientListState.errorMessage?.let { errorMessage ->
                snackbarHostState.showSnackbar(errorMessage)
            }
        }
    }

    Column(
        modifier = modifier.padding(
            top = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        ), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            OutlinedTextField(value = searchRecipientText, onValueChange = {
                searchRecipientText = it
                recipientListViewModel.onSearchTextChanged(it)
            }, label = {
                Text(text = "Search Recipient")
            }, placeholder = {
                Text(text = "Search..")
            }, trailingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            }, singleLine = true, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
            ), modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
            )

            Button(
                onClick = {
                    navigateToValidateRecipientScreen()
                }, modifier = modifier.align(Alignment.CenterVertically)
            ) {
                Text(text = "Add")
            }

        }

        if (recipientListState.isLoading) {
            Text(text = "Fetching Recipients...")
        } else {

            if (recipientListState.recipients != null) {
                LazyColumn(
                    modifier = modifier.padding(5.dp)
                ) {
                    items(recipientListState.recipients!!) { recipient ->
                        Card(
                            modifier = modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        navigateToTransactionsDetailScreen(recipient)
                                    }
                                )
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                                ) {

                                    Text(
                                        text = "${recipient.firstName} ${recipient.lastName}",
                                        modifier = modifier.padding(8.dp),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = recipient.mobilePhone,
                                        modifier = modifier.padding(8.dp),
                                        fontSize = 15.sp
                                    )

                                }

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send Money",
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clickable(onClick = {
                                            val recipientPresentation = RecipientPresentation(
                                                country = "Kenya",
                                                displayName = "${recipient.firstName} ${recipient.lastName}",
                                                phone = recipient.mobilePhone,
                                                usageLocation = recipient.usageLocation,
                                                id = recipient.objectId,
                                                email = "",
                                                firstName = recipient.firstName,
                                                lastName = recipient.lastName
                                            )
                                            navigateToPaymentFormScreen(recipientPresentation)
                                        }),
                                    tint = MaterialTheme.colorScheme.secondary
                                )

                            }


                        }
                    }
                }
            } else {
                Text(text = "You do not have any saved recipients!")
            }
        }
    }
}