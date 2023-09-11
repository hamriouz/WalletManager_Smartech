package com.example.walletmanager.service

import com.example.walletmanager.UserController
import com.example.walletmanager.controller.exception.InvalidUsernameException
import com.example.walletmanager.controller.exception.UserNotFoundException
import com.example.walletmanager.model.*
import com.example.walletmanager.model.response.FullUserResponse
import com.example.walletmanager.repository.UserRepository
import com.example.walletmanager.repository.WalletRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
class UserService(
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository,
) : UserController {
    override fun getById(id: Int): FullUserResponse {
        validateUserId(id)
        val user = userRepository.findById(id)
        val wallet = walletRepository.findByUserId(id)
        return FullUserResponse(id, user.get().ownerName, wallet!!.balance, ResponseResult.OK)
    }

    @Transactional
    override fun create(userName: SetOrChangeName): FullUserResponse? {
        validateName(userName.name)
        val user = userRepository.save(User(name = userName.name))
        val userId = user.id
        walletRepository.save(Wallet(userId, 0))
        return getById(userId!!)
    }

    @Transactional
    override fun rename(newName: SetOrChangeName, id: Int): FullUserResponse? {
        validateRenaming(id, newName.name)
        val user = userRepository.findById(id)
        user.get().ownerName = newName.name
        userRepository.save(user.get())
        return getById(id)
    }

    fun validateUserId(id: Int) {
        if (userRepository.findById(id).isEmpty) {
            throw UserNotFoundException("User not found!")
        }
    }

    fun validateName(name: String) {
        if (name == "" || name.isEmpty()) {
            throw InvalidUsernameException("Please provide a valid username!")
        }
    }

    fun validateRenaming(id: Int, name: String) {
        validateUserId(id)
        validateName(name)
    }
}



