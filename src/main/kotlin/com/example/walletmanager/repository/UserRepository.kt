package com.example.walletmanager.repository

import com.example.walletmanager.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<User, Int> {

}

