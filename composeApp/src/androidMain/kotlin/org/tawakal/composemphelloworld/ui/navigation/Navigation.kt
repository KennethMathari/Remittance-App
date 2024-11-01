package org.tawakal.composemphelloworld.ui.navigation

import android.app.Activity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.tawakal.composemphelloworld.ui.screens.PaymentFormScreen
import org.tawakal.composemphelloworld.mapper.toRecipientPresentation
import org.tawakal.composemphelloworld.model.createRecipient.RecipientPresentation
import org.tawakal.composemphelloworld.model.validaterecipient.ValidateRecipientDataPresentation
import org.tawakal.composemphelloworld.ui.navigation.destination.AddRecipient
import org.tawakal.composemphelloworld.ui.navigation.destination.Auth
import org.tawakal.composemphelloworld.ui.navigation.destination.PaymentForm
import org.tawakal.composemphelloworld.ui.navigation.destination.RecipientList
import org.tawakal.composemphelloworld.ui.navigation.destination.TransactionsDetails
import org.tawakal.composemphelloworld.ui.navigation.destination.ValidateRecipient
import org.tawakal.composemphelloworld.ui.screens.AuthScreen
import org.tawakal.composemphelloworld.ui.screens.CreateRecipientScreen
import org.tawakal.composemphelloworld.ui.screens.RecipientListScreen
import org.tawakal.composemphelloworld.ui.screens.TransactionsDetailsScreen
import org.tawakal.composemphelloworld.ui.screens.ValidateRecipientScreen
import kotlin.reflect.typeOf

@Composable
fun Navigation(
    navHostController: NavHostController, snackbarHostState: SnackbarHostState, activity: Activity
) {

    NavHost(
        navController = navHostController, startDestination = Auth
    ) {

        composable<Auth> {
            AuthScreen(
                activity = activity,
                navigateToRecipientListScreen = {
                    navHostController.navigate(RecipientList)
                },
                snackbarHostState = snackbarHostState,
            )
        }

        composable<RecipientList> {
            RecipientListScreen(snackbarHostState = snackbarHostState,
                navigateToPaymentFormScreen = { recipientPresentation ->
                    navHostController.navigate(PaymentForm(recipientPresentation))
                },
                navigateToValidateRecipientScreen = {
                    navHostController.navigate(ValidateRecipient)
                },
                navigateToTransactionsDetailScreen = { recipientDomain ->
                    navHostController.navigate(TransactionsDetails(recipientDomain.toRecipientPresentation()))
                })
        }

        composable<ValidateRecipient> {
            ValidateRecipientScreen(navigateToAddRecipientScreen = { validateRecipientDataPresentation ->
                navHostController.navigate(AddRecipient(validateRecipientDataPresentation))
            }, navigateToRecipientListScreen = {
                navHostController.navigate(RecipientList)
            }, snackbarHostState = snackbarHostState)
        }

        composable<AddRecipient>(
            typeMap = mapOf(typeOf<ValidateRecipientDataPresentation>() to parcelableType<ValidateRecipientDataPresentation>())
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<AddRecipient>()
            CreateRecipientScreen(validateRecipientDataPresentation = args.validateRecipientDataPresentation,
                snackbarHostState = snackbarHostState,
                navigateToPaymentFormScreen = { recipientPresentation ->
                    navHostController.navigate(PaymentForm(recipientPresentation))
                })
        }

        composable<PaymentForm>(
            typeMap = mapOf(typeOf<RecipientPresentation>() to parcelableType<RecipientPresentation>())
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<PaymentForm>()
            PaymentFormScreen(
                recipientPresentation = args.recipient, snackbarHostState = snackbarHostState
            )
        }

        composable<TransactionsDetails>(
            typeMap = mapOf(typeOf<org.tawakal.composemphelloworld.model.listrecipients.RecipientPresentation>() to parcelableType<org.tawakal.composemphelloworld.model.listrecipients.RecipientPresentation>())
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionsDetails>()
            TransactionsDetailsScreen(recipientPresentation = args.recipientDomain)
        }


    }

}