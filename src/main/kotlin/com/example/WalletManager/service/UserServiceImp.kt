package com.example.WalletManager.service

import com.example.WalletManager.UserService
import com.example.WalletManager.kafka.KafkaProducer
import com.example.WalletManager.model.*
import com.example.WalletManager.repository.TransactionResultRepository
import com.example.WalletManager.repository.UserRepository
import com.example.WalletManager.repository.WalletRepository
import org.springframework.stereotype.Service

@Service
class UserServiceImp(
    private val userDB: UserRepository,
    private val walletDB: WalletRepository,
    private val kafkaProducer: KafkaProducer,
    private val transactionES: TransactionResultRepository,
) : UserService{

    override fun getUserById(id: Int): FullUser? {
        return try {
            val user =  userDB.findById(id)
            val wallet = walletDB.findByUserId(id)
            FullUser(id, user.get().ownerName, wallet?.balance)

        } catch (e: Exception) {
            null
        }
    }

    override fun createNewUser(userName: SetOrChangeName): String {
        return try {
            val user = userDB.save(User(name = userName.name))
            val userId = user.id
            walletDB.save(Wallet(userId, 0))
            "User created with id $userId"
        } catch (e: Exception) {
            "Could not create user"
        }
    }

    override fun transaction(transaction: Transaction): String {
        return try {
            val kafkaMessage = createKafkaMessage(transaction)
            kafkaProducer.sendEvent(kafkaMessage)
            "The transaction was successfully added to the transaction queue"
        } catch (e: Exception) {
            "Something went wrong. Please try again later"
        }
    }

    private fun createKafkaMessage(transaction: Transaction): String {
        val userid = transaction.userId
        val amount = transaction.amount
        val transactionType = transaction.transactionType
        return "userid=$userid:amount=$amount:transactionType=$transactionType"
    }

    fun makeTransaction(transaction: Transaction): TransactionStatus {
        println("transaction made!!!!!")
        try {
            validateTransactionInput(transaction)

            val wallet = walletDB.findByUserId(transaction.userId) ?: throw Exception("USER NOT FOUND")
            if (transaction.transactionType == TransactionType.DECREASE) {
                if (wallet.balance!! < transaction.amount) {
                    transactionES.save(TransactionResult(false, transaction.userId, "Not Enough Balance"))
                    return TransactionStatus.NOT_ENOUGH_BALANCE
                }
                wallet.balance  = wallet.balance?.minus(transaction.amount)
                walletDB.save(wallet)
                return TransactionStatus.SUCCESSFUL
            }
            wallet.balance = wallet.balance?.plus(transaction.amount)
            walletDB.save(wallet)
            return TransactionStatus.SUCCESSFUL
        } catch (e: Exception) {
            println()
            throw e
        }
    }

    private fun validateTransactionInput(transaction: Transaction) {
        println("=-3=2-030-2=03-=02-03-02-=03-02-30-=02-30-=02-30=02-=30")

        if (transaction.amount < 0) {
            transactionES.save(TransactionResult(false, transaction.userId, "Negative Transaction Amount"))
            throw Exception("INVALID AMOUNT!")
        }
        if (transaction.transactionType != TransactionType.INCREASE && transaction.transactionType != TransactionType.DECREASE) {
            throw Exception("INVALID TRANSACTION TYPE")
        }
    }

    override fun renameUser(newName: SetOrChangeName, id: Int): String {
        return try {
            val user = userDB.findById(id)
            user.get().ownerName = newName.name
            userDB.save(user.get())
            val name = newName.name
            "User's name with userid $id was successfully changed to $name"
        } catch (e: Exception) {
            "could not rename user with the given information"
        }
    }
}
