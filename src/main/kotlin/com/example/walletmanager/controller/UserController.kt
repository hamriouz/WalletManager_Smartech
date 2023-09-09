package com.example.walletmanager


import com.example.walletmanager.model.response.FullUserResponse
import com.example.walletmanager.model.SetOrChangeName
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/user")
interface UserController {
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): FullUserResponse?

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody userName: SetOrChangeName): FullUserResponse?

    @PutMapping("/rename/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun rename(@RequestBody newName: SetOrChangeName, @PathVariable id: Int): FullUserResponse?

}

