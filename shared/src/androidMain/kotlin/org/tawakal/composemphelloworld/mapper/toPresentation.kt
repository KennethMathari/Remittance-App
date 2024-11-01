package org.tawakal.composemphelloworld.mapper

import org.tawakal.composemphelloworld.domain.model.createRecipient.RecipientDomain
import org.tawakal.composemphelloworld.model.createRecipient.RecipientPresentation
import org.tawakal.composemphelloworld.model.listrecipients.TransactionPresentation


fun RecipientDomain.toRecipientPresentation(): RecipientPresentation {
    return RecipientPresentation(
        country = this.country,
        displayName = this.displayName,
        email = this.email,
        firstName = this.firstName,
        id = this.id,
        lastName = this.lastName,
        phone = this.phone,
        usageLocation = this.usageLocation
    )
}

fun org.tawakal.composemphelloworld.domain.model.listRecipients.RecipientDomain.toRecipientPresentation(): org.tawakal.composemphelloworld.model.listrecipients.RecipientPresentation {
    return org.tawakal.composemphelloworld.model.listrecipients.RecipientPresentation(
        firstName = this.firstName,
        lastName = this.lastName,
        mobilePhone = this.mobilePhone,
        objectId = this.objectId,
        usageLocation = this.usageLocation,
        transactions = this.transactions.map { transactionDomain->
            TransactionPresentation(
                balance = transactionDomain.balance,
                recipientAmount = transactionDomain.recipientAmount,
                rate = transactionDomain.rate,
                fee = transactionDomain.fee,
                recipientCurrency = transactionDomain.recipientCurrency,
                senderCurrency = transactionDomain.senderCurrency,
                timestamp = transactionDomain.timestamp,
                status = transactionDomain.status,
                txnId = transactionDomain.txnId,
                fulfillmentTimestamp = transactionDomain.fulfillmentTimestamp,
                paymentService = transactionDomain.paymentService,
                service = org.tawakal.composemphelloworld.model.listrecipients.ServicePresentation(
                    cityCode = transactionDomain.service.cityCode,
                    paymentMode = transactionDomain.service.paymentMode,
                    serviceCode = transactionDomain.service.serviceCode
                )
            )
        }
    )
}