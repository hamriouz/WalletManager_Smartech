package com.example.walletmanager.controller

import com.example.walletmanager.WalletManagerApplicationTests
import com.example.walletmanager.kafka.KafkaConsumer
import com.example.walletmanager.model.SetOrChangeName
import com.example.walletmanager.model.Transaction
import com.example.walletmanager.model.TransactionType
import com.example.walletmanager.repository.WalletRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.Duration
import java.util.*


@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
class TransactionControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    var objectMapper: ObjectMapper,
    val walletRepository: WalletRepository,
    val consumer: KafkaConsumer

) : WalletManagerApplicationTests() {
    @Test
    fun `should start a successful transaction`() {
        val newUser = SetOrChangeName("newUser")
        mockMvc.post("/api/user") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newUser)
        }
        val newTransaction = Transaction(TransactionType.INCREASE, 1, 15000)
        mockMvc.put("/api/transaction") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newTransaction)
        }

        Awaitility.await().atMost(Duration.ofSeconds(15))
            .until { Objects.nonNull(consumer.payload) }


        val userWallet = walletRepository.findByUserId(1)
        assertEquals(userWallet!!.balance, 15000)
    }
}