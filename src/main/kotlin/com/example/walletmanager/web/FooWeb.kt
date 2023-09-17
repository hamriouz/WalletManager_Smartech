package com.example.walletmanager.web


import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/test")
interface FooWeb {
    @GetMapping("/1")
    @ResponseStatus(HttpStatus.OK)
    fun create()
}
