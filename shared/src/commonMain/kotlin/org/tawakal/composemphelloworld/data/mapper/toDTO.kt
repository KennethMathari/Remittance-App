package org.tawakal.composemphelloworld.data.mapper

import com.example.myapplication.data.model.createTransaction.CreateTransactionRequestDTO
import com.example.myapplication.domain.model.createTransaction.CreateTransactionRequestDomain
import org.tawakal.composemphelloworld.data.model.createRecipient.CreateRecipientRequestDTO
import org.tawakal.composemphelloworld.data.model.createTransaction.Payment
import org.tawakal.composemphelloworld.data.model.createTransaction.Recipient
import org.tawakal.composemphelloworld.data.model.createTransaction.Service
import org.tawakal.composemphelloworld.data.model.getRate.GetRateRequestDTO
import org.tawakal.composemphelloworld.domain.model.createRecipient.CreateRecipientRequestDomain
import org.tawakal.composemphelloworld.domain.model.getRate.GetRateRequestDomain

fun CreateRecipientRequestDomain.toCreateRecipientRequestDTO(): CreateRecipientRequestDTO{
    return CreateRecipientRequestDTO(
        country = this.country,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        phone = this.phone,
        relationship = this.relationship,
        usageLocation = this.usageLocation
    )
}

fun GetRateRequestDomain.toGetRateRequestDTO(): GetRateRequestDTO {
    return GetRateRequestDTO(
        amount = this.amount,
        curCode = this.curCode,
        receivingCountry = this.receivingCountry,
        sendingCountry = this.sendingCountry,
        service = this.service
    )
}

fun CreateTransactionRequestDomain.toCreateTransactionRequestDTO(): CreateTransactionRequestDTO {
    return CreateTransactionRequestDTO(
        paymentMethod = this.paymentMethod,
        purpose = this.purpose,
        timestamp = this.timestamp,
        senderObjectId = this.senderObjectId,
        payment = Payment(
            balance = this.payment.balance,
            fee = this.payment.fee,
            rate = this.payment.rate,
            recipientAmount = this.payment.recipientAmount,
            recipientCurrency = this.payment.recipientCurrency,
            senderCurrency = this.payment.senderCurrency
        ),
        recipient = Recipient(
            firstName = this.recipient.firstName,
            lastName = this.recipient.lastName,
            usageLocation = this.recipient.usageLocation,
            mobilePhone = this.recipient.mobilePhone,
            recipientObjectId = this.recipient.recipientObjectId,
            relationshipCode = this.recipient.relationshipCode,
        ),
        sourceOfIncomeCode = this.sourceOfIncomeCode,
        service = Service(
            cityCode = this.service.cityCode,
            paymentMode = this.service.paymentMode,
            serviceCode = this.service.serviceCode,
            serviceName = this.service.serviceName
        )

    )
}