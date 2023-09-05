package com.example.WalletManager.repository

import com.example.WalletManager.model.Wallet
import org.springframework.stereotype.Repository
import org.springframework.data.repository.CrudRepository
import java.util.Optional


@Repository
interface WalletRepository : CrudRepository<Wallet, Int> {
    fun findByUserId(userId: Int): Optional<Wallet>?
}



