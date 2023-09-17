package com.example.walletmanager.service.exception

class TransactionValidationException(message: MutableList<String>) : RuntimeException(message.toString())