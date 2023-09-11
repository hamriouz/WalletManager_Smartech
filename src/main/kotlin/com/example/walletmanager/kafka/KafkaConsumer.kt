package com.example.walletmanager.kafka

import com.example.walletmanager.service.TransactionService
import com.example.walletmanager.model.Transaction
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumer(
    private val objectMapper: ObjectMapper,
    private val transactionService: TransactionService

    ) {
    var payload: String? = null
    @KafkaListener(topics = [AppConstants.KAFKA_TRANSACTION])
    fun listen(msg: String) {
        val transaction = objectMapper.readValue(msg, Transaction::class.java)
        transactionService.start(transaction)
        payload = msg
    }

}