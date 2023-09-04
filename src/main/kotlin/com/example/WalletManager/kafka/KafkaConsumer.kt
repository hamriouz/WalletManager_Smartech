package com.example.WalletManager.kafka

import com.example.WalletManager.model.Transaction
import com.example.WalletManager.model.TransactionType
import com.example.WalletManager.service.UserServiceImp
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumer(
    private var userServiceImp: UserServiceImp
    ) {
    @KafkaListener(topics = [AppConstants.TOPIC_NAME])
    fun listen(msg:String){
        try {
            println("Message Received - $msg")
            val col = ":"
            val equal = "="
            val list = msg.split(col)
            val userId = list[0].split(equal)[1].toInt()
            val amount = list[1].split(equal)[1].toInt()
            val transactionType = list[2].split(equal)[1]

            println(userId)
            println(amount)

            userServiceImp.makeTransaction(Transaction(getTransactionType(transactionType), userId, amount))

        } catch (e: Exception) {
            println("error: ========")
            println(e)
        }
    }
    private fun getTransactionType(type: String): TransactionType {
        return if (type == "DECREASE") {
            TransactionType.DECREASE
        } else TransactionType.INCREASE

    }

}