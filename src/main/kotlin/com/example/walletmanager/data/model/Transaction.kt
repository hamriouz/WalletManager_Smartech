package com.example.walletmanager.data.model

import com.example.walletmanager.enums.TransactionType

class Transaction(
    var type: TransactionType,
    var userId: Int,
    var amount: Int
) {}
