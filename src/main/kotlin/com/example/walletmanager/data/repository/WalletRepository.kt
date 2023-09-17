package com.example.walletmanager.data.repository

import com.example.walletmanager.data.model.Wallet
import org.springframework.stereotype.Repository
import org.springframework.data.repository.CrudRepository


@Repository
interface WalletRepository : CrudRepository<Wallet, Int> {
    fun findByUserId(userId: Int): Wallet?
}



