package org.tawakal.composemphelloworld.model.listrecipients

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class ServicePresentation(
    val cityCode: String, val paymentMode: String, val serviceCode: String
): Parcelable
