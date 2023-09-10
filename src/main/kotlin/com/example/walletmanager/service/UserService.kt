package com.example.walletmanager.service

import com.example.walletmanager.UserController
import com.example.walletmanager.controller.exception.InvalidUsernameException
import com.example.walletmanager.controller.exception.RenamingExceptionController
import com.example.walletmanager.controller.exception.UserNotFoundException
import com.example.walletmanager.model.*
import com.example.walletmanager.model.response.FullUserResponse
import com.example.walletmanager.repository.UserRepository
import com.example.walletmanager.repository.WalletRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import javax.transaction.Transactional
import org.springframework.web.server.ResponseStatusException


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
        val errors = validateRenaming(id, newName.name)
        if (errors.isEmpty()) {
            val user = userRepository.findById(id)
            user.get().ownerName = newName.name
            userRepository.save(user.get())
            return getById(id)
        }
        val ex = RenamingExceptionController(errors.toString())
        throw ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,ex.message, ex)
    }

    fun validateUserId(id: Int) {
        if (userRepository.findById(id).isEmpty) {
            val ex = UserNotFoundException("User not found!")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, ex.message, ex)
        }
    }

    fun validateName(name: String) {
        if (name == "" || name.isEmpty()) {
            val ex = InvalidUsernameException("Please provide a valid username!")
            throw ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.message, ex)
        }
    }

    fun validateRenaming(id: Int, name: String) : MutableList<String> {
        val errors = mutableListOf<String>()
        if (userRepository.findById(id).isEmpty) {
            errors.add("User not found!")
        }
        if (name == "" || name.isEmpty()) {
            errors.add("Please provide a valid username!")
        }
        return errors
    }
}



