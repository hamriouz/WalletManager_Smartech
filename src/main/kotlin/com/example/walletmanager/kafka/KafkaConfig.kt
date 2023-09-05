package com.example.walletmanager.kafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.*
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaConfig {
    @Bean
    fun topicCreate(): NewTopic {
        return TopicBuilder.name(AppConstants.KAFKA_TRANSACTION).build()
    }
}