package com.example.walletmanager.service.exception

class InvalidUserDetailException(message: MutableList<String>) : RuntimeException(message.toString())