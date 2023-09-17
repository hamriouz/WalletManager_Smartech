package com.example.walletmanager.web

import com.example.walletmanager.WalletManagerApplicationTests
import com.example.walletmanager.data.model.User
import com.example.walletmanager.data.model.Wallet
import com.example.walletmanager.data.repository.UserRepository
import com.example.walletmanager.data.repository.WalletRepository
import com.example.walletmanager.service.UserService
import com.example.walletmanager.web.model.request.CreateUserRequest
import com.example.walletmanager.web.model.request.RenameUserRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*

class UserUnitTests: WalletManagerApplicationTests() {
    final val mockUserRepository: UserRepository = Mockito.mock(UserRepository::class.java)
    final val mockWalletRepository = Mockito.mock(WalletRepository::class.java)
    val userService = UserService(mockUserRepository, mockWalletRepository)

    @Test
    fun `should create new user`() {
        val name = "NewUserName"
        val phoneNumber = "09123456789"
        val newUser = User(name, phoneNumber)
        val userId = 1
        newUser.id = userId

        Mockito.`when`(mockUserRepository.save(Mockito.any())).thenAnswer {
            if (it.arguments[0] is User) {
                val user = it.arguments[0] as User
                user.id = userId
                return@thenAnswer user
            } else
                throw IllegalStateException("Invalid argument passed to the save method.")
        }

        val newWallet = Wallet(0, newUser)
        newWallet.user!!.id = userId
        Mockito.`when`(mockWalletRepository.save(Mockito.any())).thenReturn(newWallet)

        Mockito.`when`(mockUserRepository.findById(userId)).thenReturn(Optional.of(newUser))
        Mockito.`when`(mockWalletRepository.findByUserId(userId)).thenReturn(newWallet)

        val user = userService.create(CreateUserRequest(name, phoneNumber))
        assertEquals(user!!.userId, userId)
        assertEquals(user.balance, 0)
        assertEquals(user.name, name)
        assertEquals(user.phoneNumber, phoneNumber)
    }

    @Test
    fun `should rename user`() {
        val name = "NewUserName"
        val phoneNumber = "09987654321"
        val userId = 2
        val newUser = User(name, phoneNumber)
        newUser.id = userId
        Mockito.`when`(mockUserRepository.save(Mockito.any())).thenAnswer{
            if (it.arguments[0] is User) {
                val user = it.arguments[0] as User
                user.id = userId
                return@thenAnswer user
            } else
                throw IllegalStateException("Invalid argument passed to the save method.")
        }

        val newWallet = Wallet(0, newUser)
        newWallet.user!!.id = userId
        Mockito.`when`(mockWalletRepository.save(Mockito.any())).thenReturn (newWallet)

        Mockito.`when`(mockUserRepository.findById(userId)).thenReturn(Optional.of(newUser))
        Mockito.`when`(mockWalletRepository.findByUserId(userId)).thenReturn(newWallet)


        userService.create(CreateUserRequest(name, phoneNumber))

        val newName = "ChangedName"

        val userWithChangedUsername = userService.rename(RenameUserRequest(newName), userId)
        assertEquals(userWithChangedUsername!!.userId, userId)
        assertEquals(userWithChangedUsername.name, newName)
        assertEquals(userWithChangedUsername.phoneNumber, phoneNumber)
    }
}