package com.example.walletmanager.model

class Transaction(
    var type: TransactionType,
    var userId: Int,
    var amount: Int
) {}
