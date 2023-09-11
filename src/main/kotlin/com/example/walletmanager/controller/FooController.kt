package com.example.walletmanager.controller


import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/test")
interface FooController {
    @GetMapping("/1")
    @ResponseStatus(HttpStatus.OK)
    fun create()
}
