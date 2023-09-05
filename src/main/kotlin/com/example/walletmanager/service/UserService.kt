package com.example.walletmanager.service

import com.example.walletmanager.UserController
import com.example.walletmanager.model.*
import com.example.walletmanager.model.response.FullUserResponse
import com.example.walletmanager.repository.UserRepository
import com.example.walletmanager.repository.WalletRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
class UserService(
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository,
) : UserController {
    override fun getById(id: Int): FullUserResponse {
        val user = userRepository.findById(id)
        val wallet = walletRepository.findByUserId(id)
        return FullUserResponse(id, user.get().ownerName, wallet!!.balance, ResponseResult.OK)
    }

    @Transactional
    override fun create(userName: SetOrChangeName): FullUserResponse? {
        val user = userRepository.save(User(name = userName.name))
        val userId = user.id
        walletRepository.save(Wallet(userId, 0))
        return getById(userId!!)
    }

    @Transactional
    override fun rename(newName: SetOrChangeName, id: Int): FullUserResponse? {
        val user = userRepository.findById(id)
        user.get().ownerName = newName.name
        userRepository.save(user.get())
        return getById(id)
    }
}

