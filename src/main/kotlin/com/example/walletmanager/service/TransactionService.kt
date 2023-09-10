package com.example.walletmanager.service

import com.example.walletmanager.controller.TransactionController
import com.example.walletmanager.controller.exception.TransactionValidationException
import com.example.walletmanager.kafka.AppConstants
import com.example.walletmanager.model.ResponseResult
import com.example.walletmanager.model.Transaction
import com.example.walletmanager.model.TransactionResult
import com.example.walletmanager.model.TransactionType
import com.example.walletmanager.model.response.TransactionResponse
import com.example.walletmanager.repository.TransactionResultRepository
import com.example.walletmanager.repository.WalletRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import javax.transaction.Transactional

@Service
class TransactionService(
    private val walletRepository: WalletRepository,
    private val transactionResultRepository: TransactionResultRepository,
    private val objectMapper: ObjectMapper,
    val kafkaTemplate: KafkaTemplate<String, String>,
): TransactionController {
    var logger = LoggerFactory.getLogger(TransactionService::class.java)!!

    override fun create(transaction: Transaction): TransactionResponse? {
        val errors = validateInput(transaction)
        if (errors.isEmpty()) {
            kafkaTemplate.send(AppConstants.KAFKA_TRANSACTION, objectMapper.writeValueAsString(transaction))
            return TransactionResponse(ResponseResult.OK)
        }
        val ex = TransactionValidationException(errors.toString())
        throw ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.message, ex)
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
        transactionResultRepository.save(TransactionResult(true, transaction.userId, "None"))
    }

    fun validateInput(transaction: Transaction): MutableList<String> {
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
        return errors
    }

}