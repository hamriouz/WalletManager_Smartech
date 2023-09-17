package com.example.walletmanager.data.model

import java.util.*
import javax.persistence.*


@Entity
@Table(name = "users")
class User() {
    constructor(name: String, phoneNumber: String) : this() {
        this.ownerName = name
        this.phoneNumber = phoneNumber
    }

    @Id
    @GeneratedValue
    var id: Int? = null

    @Column(name = "owner_name", nullable = false)
    var ownerName: String? = null

    @Column(name = "phone_number", nullable = false)
    var phoneNumber: String? = null

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    var wallet: Wallet? = null
}