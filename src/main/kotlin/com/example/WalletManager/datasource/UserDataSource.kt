package com.example.WalletManager.datasource

import com.example.WalletManager.model.User


interface UserDataSource {
    fun retrieveUsers(): Collection<User>

    fun retrieveUser(): User

}