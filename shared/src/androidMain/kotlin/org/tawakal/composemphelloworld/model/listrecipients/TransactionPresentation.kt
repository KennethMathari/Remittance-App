package org.tawakal.composemphelloworld.model.listrecipients

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class TransactionPresentation(
    val balance: Double,
    val fee: Double,
    val fulfillmentTimestamp: String,
    val paymentService: String,
    val rate: Double,
    val recipientAmount: Double,
    val recipientCurrency: String,
    val senderCurrency: String,
    val service: ServicePresentation,
    val status: String,
    val timestamp: String,
    val txnId: String
) : Parcelable
