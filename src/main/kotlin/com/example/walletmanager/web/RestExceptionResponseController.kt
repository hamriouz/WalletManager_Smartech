package com.example.walletmanager.web

import com.example.walletmanager.service.exception.InvalidUserDetailException
import com.example.walletmanager.service.exception.TransactionValidationException
import com.example.walletmanager.service.exception.UserNotFoundException
import com.example.walletmanager.web.model.response.ArrayErrorMessage
import com.example.walletmanager.web.model.response.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class RestExceptionResponseController: ResponseEntityExceptionHandler() {
    @ExceptionHandler(UserNotFoundException::class)
    fun handleNotFoundException(ex: UserNotFoundException) : ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.NOT_FOUND.value(),
            ex.message!!
        )
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidUserDetailException::class, TransactionValidationException::class)
    fun handleInvalidUsernameException(ex: RuntimeException) : ResponseEntity<ArrayErrorMessage> {
        val errorMessage = ArrayErrorMessage(
            HttpStatus.NOT_ACCEPTABLE.value(),
            ex.message!!
        )
        return ResponseEntity(errorMessage, HttpStatus.NOT_ACCEPTABLE)
    }
}