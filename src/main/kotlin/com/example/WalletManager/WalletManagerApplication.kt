package com.example.WalletManager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class WalletManagerApplication

fun main(args: Array<String>) {
	runApplication<WalletManagerApplication>(*args)
}
