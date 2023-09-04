package com.example.WalletManager.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "user_wallet")


class Wallet () {
    constructor(userId: Int?, balance: Int): this() {
        this.userId = userId
        this.balance = 0
    }

    @Id
    @GeneratedValue
    val id: Int? = null

    @Column(nullable = false)
    var userId: Int? = null

    @Column(nullable = false)
    var balance: Int? = null
}