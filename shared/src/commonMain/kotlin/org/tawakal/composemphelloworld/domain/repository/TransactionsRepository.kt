package org.tawakal.composemphelloworld.domain.repository

import com.example.myapplication.domain.model.createTransaction.CreateTransactionRequestDomain
import org.tawakal.composemphelloworld.domain.NetworkResult
import org.tawakal.composemphelloworld.domain.model.getRate.GetRateRequestDomain
import org.tawakal.composemphelloworld.domain.model.getRate.GetRateResponseDomain


interface TransactionsRepository {

    suspend fun getRate(getRateRequestDomain: GetRateRequestDomain): NetworkResult<GetRateResponseDomain>

    suspend fun createTransaction(createTransactionRequestDomain: CreateTransactionRequestDomain): NetworkResult<String>
}