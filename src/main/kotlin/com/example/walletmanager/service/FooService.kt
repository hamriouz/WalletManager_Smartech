package com.example.walletmanager.service

import com.example.walletmanager.controller.FooController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Service
//@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)

class FooService : FooController{
    var counter = 1
    var logger = LoggerFactory.getLogger(TransactionService::class.java)!!

    override fun create() {
        logger.info("FooService::create::counter=$counter")
        counter += 1
    }
}