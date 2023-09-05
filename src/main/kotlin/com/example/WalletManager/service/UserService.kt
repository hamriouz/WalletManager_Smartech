package com.example.WalletManager


import com.example.WalletManager.model.response.FullUserResponse
import com.example.WalletManager.model.Transaction
import com.example.WalletManager.model.SetOrChangeName
import com.example.WalletManager.model.response.TransactionResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/Wallet/User")
interface UserService {
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Int): FullUserResponse?

    @PostMapping("/Create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createNewUser(@RequestBody userName: SetOrChangeName): FullUserResponse?

    @PutMapping("/Transaction")
    @ResponseStatus(HttpStatus.OK)
    fun transaction(@RequestBody transaction: Transaction): TransactionResponse?

    @PutMapping("/Rename/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun renameUser(@RequestBody newName: SetOrChangeName, @PathVariable id: Int): FullUserResponse?

}

