package com.example.WalletManager.service

import com.example.WalletManager.UserService
import com.example.WalletManager.kafka.KafkaProducer
import com.example.WalletManager.model.*
import com.example.WalletManager.repository.TransactionResultRepository
import com.example.WalletManager.repository.UserRepository
import com.example.WalletManager.repository.WalletRepository
import org.springframework.stereotype.Service
import java.util.Calendar
import javax.transaction.Transactional
import com.fasterxml.jackson.databind.ObjectMapper


@Service
class UserServiceImp(
    private val userDB: UserRepository,
    private val walletDB: WalletRepository,
    private val kafkaProducer: KafkaProducer,
    private val transactionES: TransactionResultRepository,
    private val objectMapper: ObjectMapper,
) : UserService {

    override fun getUserById(id: Int): FullUser {
        val user = userDB.findById(id)
        val wallet = walletDB.findByUserId(id)
        println(wallet)
        return FullUser(id, user.get().ownerName, wallet!!.get().balance)
    }

    override fun createNewUser(userName: SetOrChangeName): String {
        val user = userDB.save(User(name = userName.name))
        val userId = user.id
        walletDB.save(Wallet(userId, 0))
        return "User created with id $userId"
    }

    override fun transaction(transaction: Transaction): String {
        val kafkaMessage = objectMapper.writeValueAsString(transaction)
        kafkaProducer.sendEvent(kafkaMessage)
        return "The transaction was successfully added to the transaction queue"
    }

    @Transactional
    fun makeTransaction(transaction: Transaction) {
        println("transaction made!!!!!")
        val now = Calendar.getInstance()
        val transactionResult = TransactionResult()
        validateTransactionInput(transaction)
        val wallet = walletDB.findByUserId(transaction.userId)!!.get()
        if (transaction.transactionType == TransactionType.DECREASE) {
            if (wallet.balance!! < transaction.amount) {
                transactionResult.id = "$now-${transaction.userId}"
                transactionResult.userId = transaction.userId
                transactionResult.failureReason = "NotEnoughBalance"
                transactionResult.isSuccessful = false
//                transactionES.save(transactionResult)
            }
            wallet.balance = wallet.balance?.minus(transaction.amount)
            walletDB.save(wallet)
        }
        wallet.balance = wallet.balance?.plus(transaction.amount)
        walletDB.save(wallet)
    }

    private fun validateTransactionInput(transaction: Transaction) {
        println("=-3=2-030-2=03-=02-03-02-=03-02-30-=02-30-=02-30=02-=30")

        if (transaction.amount < 0) {
//            transactionES.save(TransactionResult(false, transaction.userId, "Negative Transaction Amount"))
            throw Exception("INVALID AMOUNT!")
        }
        if (transaction.transactionType != TransactionType.INCREASE && transaction.transactionType != TransactionType.DECREASE) {
            throw Exception("INVALID TRANSACTION TYPE")
        }
    }

    override fun renameUser(newName: SetOrChangeName, id: Int): String {
        val user = userDB.findById(id)
        user.get().ownerName = newName.name
        userDB.save(user.get())
        val name = newName.name
        return "User's name with userid $id was successfully changed to $name"
    }
}

