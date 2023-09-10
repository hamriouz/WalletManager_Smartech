package com.example.walletmanager.controller.exception

import com.example.walletmanager.model.response.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.NOT_FOUND.value(),
            ex.message!!
        )
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }
}