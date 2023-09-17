package com.example.walletmanager.web

import com.example.walletmanager.WalletManagerApplicationTests
import com.example.walletmanager.enums.TransactionType
import com.example.walletmanager.service.TransactionService
import com.example.walletmanager.service.UserService
import com.example.walletmanager.service.exception.TransactionValidationException
import com.example.walletmanager.web.model.request.CreateUserRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import com.example.walletmanager.web.model.request.TransactionRequest
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

class TransactionUnitTest @Autowired constructor(
    private val transactionService: TransactionService,
    private val userService: UserService,
) : WalletManagerApplicationTests() {
    @Test
    fun `should throw exception on negative amount input`() {
        val transaction = TransactionRequest(TransactionType.DECREASE, 123, -100)
        assertThrows<TransactionValidationException>("[Negative Transaction Amount]") {
            transactionService.validateInput(
                transaction
            )
        }
    }

    @Test
    fun `should throw exception on invalid userId input when user doesnt exist`() {
        val transaction = TransactionRequest(TransactionType.DECREASE, 123, 100)
        assertThrows<TransactionValidationException>("[Invalid userId input]") {
            transactionService.validateInput(transaction)
        }
    }

    @Test
    fun `should not throw any exception with valid input`() {
        val username = "neeeewNaame"
        val phoneNumber = "09123456789"
        val user = userService.create(CreateUserRequest(username, phoneNumber))

        val transaction = TransactionRequest(TransactionType.DECREASE, user!!.userId, 100)
        assertDoesNotThrow {
            transactionService.validateInput(transaction)
        }
    }
}