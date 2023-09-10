package com.example.walletmanager.controller

import com.example.walletmanager.model.Transaction
import com.example.walletmanager.model.response.TransactionResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/transaction")
interface TransactionController {
    @PutMapping("")
    fun create(@RequestBody transaction: Transaction): TransactionResponse?
}