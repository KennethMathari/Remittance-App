package org.tawakal.composemphelloworld.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tawakal.composemphelloworld.model.listrecipients.RecipientPresentation

@Composable
fun TransactionsDetailsScreen(
    recipientPresentation: RecipientPresentation,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            top = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        ), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "${recipientPresentation.firstName}'s transaction history")

        LazyColumn(
            modifier = modifier.padding(5.dp)
        ) {
            items(recipientPresentation.transactions){ transactionPresentation->
                Card(
                    modifier = modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ){
                    Text(text = transactionPresentation.status )
                    Text(text = transactionPresentation.recipientAmount.toString() )
                    Text(text = transactionPresentation.timestamp )
                }

            }

        }
    }

}