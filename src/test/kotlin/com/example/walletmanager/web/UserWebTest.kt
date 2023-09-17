package com.example.walletmanager.web

import com.example.walletmanager.WalletManagerApplicationTests
import com.example.walletmanager.data.repository.UserRepository
import com.example.walletmanager.web.model.request.CreateUserRequest
import com.example.walletmanager.web.model.request.RenameUserRequest
import com.example.walletmanager.web.model.response.UserWithBalanceDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class UserWebTest (
    val mockMvc: MockMvc,
    var objectMapper: ObjectMapper,
    val userRepository: UserRepository

) : WalletManagerApplicationTests() {
    @Test
    fun `should create user`() {
        val name = "hamraz"
        val phoneNumber = "09123456789"
        val newUser = CreateUserRequest(name, phoneNumber)

        val result = mockMvc.perform(post("/api/user")
            .content(objectMapper.writeValueAsString(newUser))
            .contentType(MediaType.APPLICATION_JSON)
        )

        val user = objectMapper.readValue(result.andReturn().response.contentAsString, UserWithBalanceDTO::class.java)
        assertEquals(name, userRepository.findById(user.userId).get().ownerName)
    }

    @Test
    fun `should rename user`() {
        val name = "hamraz"
        val phoneNumber = "09123456789"
        val newUser = CreateUserRequest(name, phoneNumber)

        val result = mockMvc.perform(post("/api/user")
            .content(objectMapper.writeValueAsString(newUser))
            .contentType(MediaType.APPLICATION_JSON)
        )

        val createdUser = objectMapper.readValue(result.andReturn().response.contentAsString, UserWithBalanceDTO::class.java)

        val newName = RenameUserRequest("changedName")
        mockMvc.put("/api/user/rename/${createdUser.userId}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newName)
        }
        val user = userRepository.findById(createdUser.userId)
        assertEquals(newName.newName, user.get().ownerName)
    }
}