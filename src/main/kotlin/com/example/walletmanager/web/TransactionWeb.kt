package com.example.walletmanager.web

import com.example.walletmanager.data.model.Transaction
import com.example.walletmanager.web.model.request.TransactionRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/transaction")
interface TransactionWeb {
    @PutMapping("")
    fun create(@RequestBody transaction: TransactionRequest)
}