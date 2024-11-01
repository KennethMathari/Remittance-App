package org.tawakal.composemphelloworld.model.listrecipients

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class RecipientPresentation(
    val firstName: String,
    val lastName: String,
    val mobilePhone: String,
    val objectId: String,
    val transactions: List<TransactionPresentation>,
    val usageLocation: String
) : Parcelable
