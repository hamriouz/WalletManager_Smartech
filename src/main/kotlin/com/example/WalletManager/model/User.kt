package com.example.WalletManager.model

import javax.persistence.*

@Entity
@Table(name = "users")
class User() {
    constructor(name: String) : this() {
        this.ownerName = name
    }

    @Id
    @GeneratedValue
    var id: Int? = null

    @Column(name = "owner_name", nullable = false)
    var ownerName: String? = null

}