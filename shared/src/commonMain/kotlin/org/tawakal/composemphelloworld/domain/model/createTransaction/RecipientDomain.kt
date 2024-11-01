package com.example.myapplication.domain.model.createTransaction

data class RecipientDomain(
    val firstName: String,
    val lastName: String,
    val mobilePhone: String,
    val recipientObjectId: String,
    val relationshipCode: String,
    val usageLocation: String
)
