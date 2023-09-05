package com.example.walletmanager

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest(
    classes = [WalletManagerApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class WalletManagerApplicationTests(
) {

    @Test
    fun contextLoads() {
    }

}
