package com.example.walletmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WalletManagerApplication

fun main(args: Array<String>) {
    runApplication<WalletManagerApplication>(*args)
}
