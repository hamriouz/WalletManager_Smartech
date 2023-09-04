package com.example.WalletManager.kafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.*
import org.springframework.kafka.config.TopicBuilder

@Configuration
open class KafkaConfig {
    @Bean
    open fun topicCreate(): NewTopic {
        return TopicBuilder.name(AppConstants.TOPIC_NAME).build()
    }
}