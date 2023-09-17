package com.example.walletmanager.service

import com.example.walletmanager.web.TransactionWeb
import com.example.walletmanager.service.exception.TransactionValidationException
import com.example.walletmanager.service.kafka.AppConstants
import com.example.walletmanager.data.model.Transaction
import com.example.walletmanager.data.model.TransactionElasticSearch
import com.example.walletmanager.enums.TransactionType
import com.example.walletmanager.data.repository.TransactionResultRepository
import com.example.walletmanager.data.repository.WalletRepository
import com.example.walletmanager.web.model.request.TransactionRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class TransactionService(
    private val walletRepository: WalletRepository,
    private val transactionResultRepository: TransactionResultRepository,
    private val objectMapper: ObjectMapper,
    val kafkaTemplate: KafkaTemplate<String, String>,
) : TransactionWeb {
    var logger = LoggerFactory.getLogger(TransactionService::class.java)!!

    override fun create(transaction: TransactionRequest) {
        validateInput(transaction)
        kafkaTemplate.send(AppConstants.KAFKA_TRANSACTION, objectMapper.writeValueAsString(transaction))
    }

    fun makeTransaction2(transaction: Transaction) {
        this.start(transaction)
    }

    @Transactional
    fun start(transaction: Transaction) {
        val wallet = walletRepository.findByUserId(transaction.userId)!!
        if (transaction.type == TransactionType.DECREASE && wallet.balance!! >= transaction.amount) {
            wallet.balance = wallet.balance?.minus(transaction.amount)
        } else if (transaction.type == TransactionType.INCREASE) {
            wallet.balance = wallet.balance?.plus(transaction.amount)
        }
        walletRepository.save(wallet)
//        throw RuntimeException("Test")
        logger.info("TransactionService::makeTransaction::userId=${transaction.userId}::amount=${transaction.amount}::type=${transaction.type}")
        transactionResultRepository.save(TransactionElasticSearch(transaction.userId, transaction.amount, transaction.type))
    }

    fun validateInput(transaction: TransactionRequest) {
        val errors = mutableListOf<String>()
        var errorMessage: String?
        if (transaction.amount < 0) {
            errorMessage = "Negative Transaction Amount"
            logger.error(errorMessage)
            errors.add(errorMessage)
        }

        if (walletRepository.findByUserId(transaction.userId) == null) {
            errorMessage = "Invalid userId input"
            errors.add(errorMessage)
        }
        if (errors.isNotEmpty()) {
            throw TransactionValidationException(errors)
        }
    }

}