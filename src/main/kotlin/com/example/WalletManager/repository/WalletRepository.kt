package com.example.WalletManager.repository

import com.example.WalletManager.model.Wallet
import org.springframework.stereotype.Repository
import org.springframework.data.repository.CrudRepository


@Repository
interface WalletRepository : CrudRepository<Wallet, Int> {
    fun findByUserId(userId: Int): Wallet?
}



