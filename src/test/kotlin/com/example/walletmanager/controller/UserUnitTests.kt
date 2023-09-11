package com.example.walletmanager.controller

import com.example.walletmanager.WalletManagerApplicationTests
import com.example.walletmanager.model.SetOrChangeName
import com.example.walletmanager.model.User
import com.example.walletmanager.model.Wallet
import com.example.walletmanager.repository.UserRepository
import com.example.walletmanager.repository.WalletRepository
import com.example.walletmanager.service.UserService
import org.mockito.Mockito
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import java.util.*

@AutoConfigureMockMvc
class UserUnitTests: WalletManagerApplicationTests() {
    final val mockUserRepository: UserRepository = Mockito.mock(UserRepository::class.java)
    final val mockWalletRepository = Mockito.mock(WalletRepository::class.java)
    @Autowired
    val userService = UserService(mockUserRepository, mockWalletRepository)


    @Test
    fun `should create new user`() {
        val newUser = User("NewUserName")
        newUser.id = 1
        Mockito.`when`(mockUserRepository.save(User("NewUserName"))).thenReturn(newUser)

        val newWallet = Wallet(1, 0)
        newWallet.userId = 1
        Mockito.`when`(mockWalletRepository.save(Wallet(1, 0))).thenReturn(newWallet)

        Mockito.`when`(mockUserRepository.findById(1)).thenReturn(Optional.of(newUser))
        Mockito.`when`(mockWalletRepository.findByUserId(1)).thenReturn(newWallet)

        val user = userService.create(SetOrChangeName("NewUserName"))
        assertEquals(user!!.userId, 1)
        assertEquals(user.balance, 0)
        assertEquals(user.userName, "NewUserName")
    }

    @Test
    fun `should rename user`() {
        val newUser = User("NewUserName")
        newUser.id = 3
        Mockito.`when`(mockUserRepository.save(User("NewUserName"))).thenReturn(newUser)

        val newWallet = Wallet(3, 0)
        newWallet.userId = 3
        Mockito.`when`(mockWalletRepository.save(Wallet(3, 0))).thenReturn(newWallet)

        Mockito.`when`(mockUserRepository.findById(3)).thenReturn(Optional.of(newUser))
        Mockito.`when`(mockWalletRepository.findByUserId(3)).thenReturn(newWallet)

        userService.create(SetOrChangeName("NewUserName"))
        val userWithChangedUsername = userService.rename(SetOrChangeName("ChangedUserName"), 3)
        assertEquals(userWithChangedUsername!!.userId, 3)
        assertEquals(userWithChangedUsername.userName, "ChangedUserName")
    }

}