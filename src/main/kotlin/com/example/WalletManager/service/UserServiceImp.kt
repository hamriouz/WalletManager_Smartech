package com.example.WalletManager.service

import com.example.WalletManager.UserService
import com.example.WalletManager.kafka.KafkaProducer
import com.example.WalletManager.model.*
import com.example.WalletManager.repository.TransactionResultRepository
import com.example.WalletManager.repository.UserRepository
import com.example.WalletManager.repository.WalletRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*


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
        return FullUser(id, user.get().ownerName, wallet!!.get().balance)
    }

    @Transactional
    override fun createNewUser(userName: SetOrChangeName): Int {
        val user = userDB.save(User(name = userName.name))
        val userId = user.id
        walletDB.save(Wallet(userId, 0))
        return userId!!
    }

    override fun transaction(transaction: Transaction): String {
        val isValid = validateTransactionInput(transaction)
        return if (isValid) {
            val kafkaMessage = objectMapper.writeValueAsString(transaction)
            kafkaProducer.sendEvent(kafkaMessage)
            "The transaction was successfully added to the transaction queue"
        } else "Invalid input!"
    }

    @Transactional
    fun makeTransaction(transaction: Transaction) {
        val wallet = walletDB.findByUserId(transaction.userId)!!.get()
        if (transaction.transactionType == TransactionType.DECREASE) {
            if (wallet.balance!! < transaction.amount) {
                val transactionResult = TransactionResult(false, transaction.userId, "User does not have enough balance")
                transactionES.save(transactionResult)
            } else {
                wallet.balance = wallet.balance?.minus(transaction.amount)
                walletDB.save(wallet)
                transactionES.save(TransactionResult(true, transaction.userId, "None"))
            }
        } else {
            wallet.balance = wallet.balance?.plus(transaction.amount)
            walletDB.save(wallet)
            transactionES.save(TransactionResult(true, transaction.userId, "None"))
        }
    }

    fun validateTransactionInput(transaction: Transaction): Boolean {
        if (transaction.amount < 0) {
            transactionES.save(TransactionResult(false, transaction.userId, "Negative Transaction Amount"))
            return false
        }
        if (transaction.transactionType != TransactionType.INCREASE && transaction.transactionType != TransactionType.DECREASE) {
            transactionES.save(TransactionResult(false, transaction.userId, "Invalid TransactionType as Input"))
            return false
        }
        if (Optional.empty<Wallet>() ==  walletDB.findByUserId(transaction.userId)) {
            transactionES.save(TransactionResult(false, transaction.userId, "Invalid userId input"))
            return false
        }
        return true
    }

    @Transactional
    override fun renameUser(newName: SetOrChangeName, id: Int): String {
        val user = userDB.findById(id)
        user.get().ownerName = newName.name
        userDB.save(user.get())
        val name = newName.name
        return "User's name with userid $id was successfully changed to $name"
    }
}

