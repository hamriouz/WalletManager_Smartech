package com.example.WalletManager.service

import com.example.WalletManager.WalletManagerApplicationTests
import com.example.WalletManager.model.SetOrChangeName
import com.example.WalletManager.model.Transaction
import com.example.WalletManager.model.TransactionType
import com.example.WalletManager.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@AutoConfigureMockMvc
class UserServiceImpTest @Autowired constructor(
    val userServiceImp: UserServiceImp,
    val mockMvc: MockMvc,
    var objectMapper: ObjectMapper,
    val userRepository: UserRepository

) : WalletManagerApplicationTests() {

    @Test
    fun `should create user`() {
        val newUser = SetOrChangeName("newUser")
        mockMvc.post("/api/Wallet/User/Create") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newUser)
        }
        val user = userRepository.findById(3)
        assertEquals("newUser", user.get().ownerName)
    }

    @Test
    fun `should rename user`() {
        val newUser = SetOrChangeName("newUser")
        mockMvc.post("/api/Wallet/User/Create") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newUser)
        }
        val newName = SetOrChangeName("changedName")
        val userId = 1
        mockMvc.put("/api/Wallet/User/Rename/$userId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newName)
        }
        val user = userRepository.findById(1)
        assertEquals("changedName", user.get().ownerName)
    }


    @Test
    fun `should return false when transaction amount is negative`() {
        val transaction = Transaction(TransactionType.DECREASE, 123, -100)
        val isValid = userServiceImp.validateTransactionInput(transaction)
        assertFalse(isValid)
    }

    @Test
    fun `should return false when userId does not exist`() {
        val transaction = Transaction(TransactionType.DECREASE, 123, 100)
        val isValid = userServiceImp.validateTransactionInput(transaction)
        assertFalse(isValid)
    }

    @Test
    fun `should return true when all fields where ok`() {
        val userId = userServiceImp.createNewUser(SetOrChangeName("usssernaaame"))
        val transaction = Transaction(TransactionType.INCREASE, userId, 1000)
        val isValid = userServiceImp.validateTransactionInput(transaction)
        assertTrue(isValid)
    }

}