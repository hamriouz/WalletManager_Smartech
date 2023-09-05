package com.example.walletmanager.model.response

import com.example.walletmanager.model.ResponseResult


class TransactionResponse(
    val transactionErrors: Array<String>,
    val status: ResponseResult,
)

