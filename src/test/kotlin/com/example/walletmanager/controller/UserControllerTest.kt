package com.example.walletmanager.controller

import com.example.walletmanager.WalletManagerApplicationTests
import com.example.walletmanager.model.SetOrChangeName
import com.example.walletmanager.repository.UserRepository
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
class UserControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    var objectMapper: ObjectMapper,
    val userRepository: UserRepository

) : WalletManagerApplicationTests() {

    @Test
    fun `should create user`() {
        val newUser = SetOrChangeName("newUser")
        mockMvc.post("/api/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newUser)
        }
        val user = userRepository.findById(1)
        assertEquals("newUser", user.get().ownerName)
    }

    @Test
    fun `should rename user`() {
        val newUser = SetOrChangeName("newUser")
        mockMvc.post("/api/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newUser)
        }
        val newName = SetOrChangeName("changedName")
        val userId = 1
        mockMvc.put("/api/user/rename/$userId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newName)
        }
        val user = userRepository.findById(1)
        assertEquals("changedName", user.get().ownerName)
    }
}