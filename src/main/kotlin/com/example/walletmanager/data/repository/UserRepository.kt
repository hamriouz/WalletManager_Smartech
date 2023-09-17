package com.example.walletmanager.data.repository

import com.example.walletmanager.data.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<User, Int> {

}

