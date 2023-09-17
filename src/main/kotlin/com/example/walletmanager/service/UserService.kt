package com.example.walletmanager.service

import com.example.walletmanager.UserWeb
import com.example.walletmanager.data.model.User
import com.example.walletmanager.data.model.Wallet
import com.example.walletmanager.service.exception.UserNotFoundException
import com.example.walletmanager.data.repository.UserRepository
import com.example.walletmanager.data.repository.WalletRepository
import com.example.walletmanager.service.exception.InvalidUserDetailException
import com.example.walletmanager.web.model.request.CreateUserRequest
import com.example.walletmanager.web.model.request.RenameUserRequest
import com.example.walletmanager.web.model.response.UserDTO
import com.example.walletmanager.web.model.response.UserWithBalanceDTO
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional


@Service
class UserService(
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository,
) : UserWeb {

    override fun getById(id: Int): UserWithBalanceDTO {
        val user = userRepository.findById(id)
        if (user.isEmpty) {
            throw UserNotFoundException("User not found!")
        }
        return UserWithBalanceDTO(user.get().ownerName!!, user.get().id!!, user.get().phoneNumber!!, user.get().wallet!!.balance!!)
    }

    @Transactional
    override fun create(userDetail: CreateUserRequest): UserWithBalanceDTO? {
        validateUserDetail(userDetail)
        val user = userRepository.save(User(userDetail.name, userDetail.phoneNumber))
        val wallet = Wallet(0, user)
        walletRepository.save(wallet)
        return UserWithBalanceDTO(user.ownerName!!, user.id!!, user.phoneNumber!!, wallet.balance!!)
    }

    @Transactional
    override fun rename(newNameReq: RenameUserRequest, id: Int): UserDTO? {
        val user = userRepository.findById(id)
        validateRenaming(user, newNameReq.newName)
        user.get().ownerName = newNameReq.newName
        userRepository.save(user.get())
        return UserDTO(user.get().ownerName!!, user.get().id!!, user.get().phoneNumber!!)
    }

    fun validateUserDetail(userDetail: CreateUserRequest) {
        val errors = mutableListOf<String>()
        if (userDetail.name == "" || userDetail.name.isEmpty()) {
            errors.add("Please provide a valid username!")
        }
        if (userDetail.phoneNumber == "" || userDetail.phoneNumber.isEmpty() || userDetail.phoneNumber.length != 11) {
            errors.add("Please provide a valid phone number")
        }
        if (errors.isNotEmpty()) {
            throw InvalidUserDetailException(errors)
        }
    }

    fun validateRenaming(user: Optional<User>, newName: String) {
        val errors = mutableListOf<String>()
        if (user.isEmpty) {
            errors.add("User not found!")
        }
        if (newName == "" || newName.isEmpty()) {
            errors.add("Please provide a valid username!")
        }
        if (errors.isNotEmpty()) {
            throw InvalidUserDetailException(errors)
        }
    }
}
