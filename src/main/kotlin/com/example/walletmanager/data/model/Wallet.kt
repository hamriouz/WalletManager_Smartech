package com.example.walletmanager.data.model

import javax.persistence.*


@Entity
@Table(name = "user_wallet")
class Wallet() {
    constructor(balance: Int, user: User) : this() {
        this.balance = 0
        this.user = user
    }

    @Id
    @GeneratedValue
    val id: Int? = null

    @Column(nullable = false)
    var balance: Int? = null

    @OneToOne
    @JoinColumn(name = "user_id")
    var user: User? = null // Foreign key referencing the User table
}