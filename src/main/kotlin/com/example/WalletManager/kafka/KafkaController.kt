package com.example.WalletManager.kafka
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RestController
class KafkaController(val kafkaProducer: KafkaProducer){
    @GetMapping("/event")
    fun sendKafkaEvent(@RequestParam("msg") msg: String): ResponseEntity<String> {
        kafkaProducer.sendEvent(msg)
        return ResponseEntity("Message sent by kafka", HttpStatus.OK)
    }
}