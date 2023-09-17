package com.example.walletmanager

import com.example.walletmanager.web.model.request.CreateUserRequest
import com.example.walletmanager.web.model.request.RenameUserRequest
import com.example.walletmanager.web.model.response.UserDTO
import com.example.walletmanager.web.model.response.UserWithBalanceDTO
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/user")
interface UserWeb {
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): UserWithBalanceDTO?

    @PostMapping
    fun create(@RequestBody userDetail: CreateUserRequest): UserWithBalanceDTO?

    @PutMapping("/rename/{id}")
    fun rename(@RequestBody newNameReq: RenameUserRequest, @PathVariable id: Int): UserDTO?

}

