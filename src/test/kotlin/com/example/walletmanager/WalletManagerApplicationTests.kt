package com.example.walletmanager

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestConstructor

@AutoConfigureMockMvc
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
@SpringBootTest(
    classes = [WalletManagerApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class WalletManagerApplicationTests {

    @Test
    fun contextLoads() {
    }

}
