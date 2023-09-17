package com.example.walletmanager.web.model.request

import com.example.walletmanager.enums.TransactionType

class TransactionRequest (
    var type: TransactionType,
    var userId: Int,
    var amount: Int
)