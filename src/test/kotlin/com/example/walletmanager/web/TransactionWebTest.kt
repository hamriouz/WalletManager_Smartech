package com.example.walletmanager.web

import com.example.walletmanager.WalletManagerApplicationTests
import com.example.walletmanager.data.model.Transaction
import com.example.walletmanager.enums.TransactionType
import com.example.walletmanager.data.repository.WalletRepository
import com.example.walletmanager.web.model.request.CreateUserRequest
import com.example.walletmanager.web.model.response.UserWithBalanceDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import java.time.Duration


class TransactionWebTest(
    val mockMvc: MockMvc,
    var objectMapper: ObjectMapper,
    val walletRepository: WalletRepository
) : WalletManagerApplicationTests() {
    @Test
    fun `should start a successful transaction`() {
        val name = "hamraz"
        val phoneNumber = "09123456789"
        val amount = 15000
        val newUser = CreateUserRequest(name, phoneNumber)

        val result = mockMvc.perform(post("/api/user")
            .content(objectMapper.writeValueAsString(newUser))
            .contentType(MediaType.APPLICATION_JSON)
        )

        val createdUser = objectMapper.readValue(result.andReturn().response.contentAsString, UserWithBalanceDTO::class.java)

        val newTransaction = Transaction(TransactionType.INCREASE, createdUser.userId, amount)
        mockMvc.put("/api/transaction") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newTransaction)
        }

        Awaitility.await().atMost(Duration.ofSeconds(1))
            .until {
                val userWallet = walletRepository.findByUserId(createdUser.userId)
                userWallet!!.balance == amount
            }
    }
}