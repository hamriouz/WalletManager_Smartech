package com.example.WalletManager.kafka

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.*

@Service
class KafkaProducer(val kafkaTemplate: KafkaTemplate<String, String>) {
    fun sendEvent(message: String) {
        kafkaTemplate.send(AppConstants.TOPIC_NAME, message)
    }
}