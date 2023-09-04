package com.example.WalletManager.kafka

import com.example.WalletManager.model.Transaction
import com.example.WalletManager.service.UserServiceImp
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumer(
    private var userServiceImp: UserServiceImp,
    private val objectMapper: ObjectMapper,

    ) {
    @KafkaListener(topics = [AppConstants.TOPIC_NAME])
    fun listen(msg: String) {
        val transaction = objectMapper.readValue(msg, Transaction::class.java)
        userServiceImp.makeTransaction(transaction)
    }

}