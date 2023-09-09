package com.example.walletmanager.service

import com.example.walletmanager.UserController
import com.example.walletmanager.model.*
import com.example.walletmanager.model.response.FullUserResponse
import com.example.walletmanager.repository.UserRepository
import com.example.walletmanager.repository.WalletRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional


@Service
class UserService(
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository,
) : UserController {
    override fun getById(id: Int): FullUserResponse {
        val errors = validateUserId(id)
        if (errors.isEmpty()) {
            val user = userRepository.findById(id)
            val wallet = walletRepository.findByUserId(id)
            return FullUserResponse(id, user.get().ownerName, wallet!!.balance, ResponseResult.OK, errors)
        }
        return FullUserResponse(id, null, null, ResponseResult.ERROR, errors)
    }

    @Transactional
    override fun create(userName: SetOrChangeName): FullUserResponse? {
        val errors = validateName(userName.name)
        if (errors.isEmpty()) {
            val user = userRepository.save(User(name = userName.name))
            val userId = user.id
            walletRepository.save(Wallet(userId, 0))
            return getById(userId!!)
        }
        return FullUserResponse(0, null, null, ResponseResult.ERROR, errors)
    }

    @Transactional
    override fun rename(newName: SetOrChangeName, id: Int): FullUserResponse? {
        val errors = validateUserId(id)
        errors += validateName(newName.name)
        val user = userRepository.findById(id)
        if (errors.isEmpty()) {
            user.get().ownerName = newName.name
            userRepository.save(user.get())
            return getById(id)
        }
        return FullUserResponse(0, null, null, ResponseResult.ERROR, errors)
    }

    fun validateUserId(id: Int): MutableList<String> {
        val errors = mutableListOf<String>()
        if (userRepository.findById(id).isEmpty) {
            errors.add("User with the given userId does not exist!")
        }
        return errors
    }

    fun validateName(name: String): MutableList<String> {
        val errors = mutableListOf<String>()
        if (name == "" || name.isEmpty()) {
            errors.add("Please provide a valid name!")
        }
        return errors
    }
}

