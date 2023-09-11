package com.example.walletmanager.controller

import com.example.walletmanager.WalletManagerApplicationTests
import com.example.walletmanager.model.Transaction
import com.example.walletmanager.model.TransactionType
import com.example.walletmanager.service.TransactionService
import com.example.walletmanager.service.UserService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import com.example.walletmanager.model.SetOrChangeName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc



@AutoConfigureMockMvc
class TransactionUnitTest @Autowired constructor(
    private val transactionService: TransactionService,
    private val userService: UserService,
): WalletManagerApplicationTests() {


    @Test
    fun `should contain amount is negative error`() {
        val transaction = Transaction(TransactionType.DECREASE, 123, -100)
        val errors = transactionService.validateInput(transaction)
        assertTrue(errors.contains("Negative Transaction Amount"))
    }

    @Test
    fun `should return false when userId does not exist`() {
        val transaction = Transaction(TransactionType.DECREASE, 123, 100)
        val errors = transactionService.validateInput(transaction)
        assertTrue(errors.contains("Invalid userId input"))
    }

    @Test
    fun `should return true when all fields where ok`() {
        val user = userService.create(SetOrChangeName("usssernaaame"))
        val transaction = user?.let { Transaction(TransactionType.INCREASE, it.userId, 1000) }
        val errors = transaction?.let { transactionService.validateInput(it) }
        assertTrue(errors!!.isEmpty())
    }
}