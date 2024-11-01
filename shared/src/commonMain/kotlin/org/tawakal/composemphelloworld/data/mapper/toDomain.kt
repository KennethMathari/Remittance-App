package org.tawakal.composemphelloworld.data.mapper

import org.tawakal.composemphelloworld.data.model.createRecipient.Data
import org.tawakal.composemphelloworld.data.model.getRate.GetRateResponseDTO
import org.tawakal.composemphelloworld.data.model.listRecipient.ListRecipientResponseDTO
import org.tawakal.composemphelloworld.data.model.validateRecipient.ValidateRecipientDataDTO
import org.tawakal.composemphelloworld.data.model.validateRecipient.ValidateRecipientResponseDTO
import org.tawakal.composemphelloworld.domain.model.createRecipient.CreateRecipientDataDomain
import org.tawakal.composemphelloworld.domain.model.createRecipient.RecipientDomain
import org.tawakal.composemphelloworld.domain.model.getRate.GetRateResponseDataDomain
import org.tawakal.composemphelloworld.domain.model.getRate.GetRateResponseDomain
import org.tawakal.composemphelloworld.domain.model.getRate.RateDomain
import org.tawakal.composemphelloworld.domain.model.listRecipients.DataDomain
import org.tawakal.composemphelloworld.domain.model.listRecipients.ListRecipientResponseDomain
import org.tawakal.composemphelloworld.domain.model.listRecipients.ServiceDomain
import org.tawakal.composemphelloworld.domain.model.validateRecipient.RecipientDataDomain
import org.tawakal.composemphelloworld.domain.model.validateRecipient.ValidateRecipientDataDomain
import org.tawakal.composemphelloworld.domain.model.validateRecipient.ValidateRecipientResponseDomain

fun ValidateRecipientResponseDTO.toValidateRecipientResponseDomain(): ValidateRecipientResponseDomain {
    return ValidateRecipientResponseDomain(
        source = this.source,
        status = this.status,
        statusCode = this.statusCode,
        timeStamp = this.timeStamp,
        validateRecipientData = this.`data`.toValidateRecipientDataDomain()
    )
}

fun ValidateRecipientDataDTO.toValidateRecipientDataDomain(): ValidateRecipientDataDomain {
    return ValidateRecipientDataDomain(
        recipient = RecipientDataDomain(
            country = this.recipient.country ?: "",
            displayName = this.recipient.displayName ?: "",
            firstName = this.recipient.firstName,
            id = this.recipient.id ?: "",
            lastName = this.recipient.lastName,
            phone = this.recipient.phone ?: "",
            usageLocation = this.recipient.usageLocation ?: "",
            email = this.recipient.email ?: ""
        )
    )
}

fun Data.toCreateRecipientDataDomain(): CreateRecipientDataDomain {
    return CreateRecipientDataDomain(
        recipient = RecipientDomain(
            country = this.recipient.country,
            displayName = this.recipient.displayName,
            email = this.recipient.email,
            firstName = this.recipient.firstName,
            lastName = this.recipient.lastName,
            id = this.recipient.id,
            phone = this.recipient.phone,
            usageLocation = this.recipient.usageLocation
        )
    )
}

fun ListRecipientResponseDTO.toListRecipientResponseDomain(): ListRecipientResponseDomain {
    return ListRecipientResponseDomain(
        message = this.message,
        statusCode = this.statusCode,
        status = this.status,
        timeStamp = this.timeStamp,
        data = DataDomain(
            recipients = this.data.recipients.map { recipient->
                org.tawakal.composemphelloworld.domain.model.listRecipients.RecipientDomain(
                    firstName = recipient.firstName,
                    lastName = recipient.lastName,
                    objectId = recipient.objectId,
                    mobilePhone = recipient.mobilePhone,
                    usageLocation = recipient.usageLocation,
                    transactions = recipient.transactions.map { transaction->
                        org.tawakal.composemphelloworld.domain.model.listRecipients.TransactionDomain(
                            balance = transaction.balance,
                            fee = transaction.fee,
                            recipientCurrency = transaction.recipientCurrency,
                            recipientAmount = transaction.recipientAmount,
                            rate = transaction.rate,
                            status = transaction.status,
                            timestamp = transaction.timestamp,
                            fulfillmentTimestamp = transaction.fulfillmentTimestamp,
                            txnId = transaction.txnId,
                            paymentService = transaction.paymentService,
                            senderCurrency = transaction.senderCurrency,
                            service = ServiceDomain(
                                paymentMode = transaction.service.paymentMode,
                                serviceCode = transaction.service.serviceCode,
                                cityCode = transaction.service.cityCode
                            )
                        )
                    }
                )
            }
        )
    )
}

fun GetRateResponseDTO.toGetRateResponseDomain(): GetRateResponseDomain {
    return GetRateResponseDomain(
        message = this.message,
        status = this.message,
        statusCode = this.statusCode,
        timeStamp = this.timeStamp,
        data = GetRateResponseDataDomain(
            recipient = RateDomain(
                maximumAmount = this.data.recipient.maximumAmount,
                minimumAmount = this.data.recipient.minimumAmount,
                payoutRate = this.data.recipient.payoutRate,
                payoutAmount = this.data.recipient.payoutAmount,
                payoutCurrency = this.data.recipient.payoutCurrency,
                payoutFee = this.data.recipient.payoutFee
            )
        )
    )
}