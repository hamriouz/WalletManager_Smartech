package com.example.walletmanager.controller.exception

import com.example.walletmanager.model.response.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class RestResponseEntityHandler: ResponseEntityExceptionHandler() {
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException) : ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.NOT_FOUND.value(),
            ex.message!!
        )
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidUsernameException::class)
    fun handleInvalidUsernameException(ex: InvalidUsernameException) : ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.NOT_ACCEPTABLE.value(),
            ex.message!!
        )
        return ResponseEntity(errorMessage, HttpStatus.NOT_ACCEPTABLE)
    }

    @ExceptionHandler(TransactionValidationException::class)
    fun handleTransactionException(ex: TransactionValidationException) : ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.NOT_ACCEPTABLE.value(),
            ex.message!!
        )
        return ResponseEntity(errorMessage, HttpStatus.NOT_ACCEPTABLE)    }

//    @ExceptionHandler(value = [InvalidUsernameException::class, UserNotFoundException::class])
//    fun handleRenamingException(ex: InvalidUsernameException, ex2: UserNotFoundException) : ResponseEntity<ErrorMessage> {
//        val errors = arrayOf(ex.message, ex2.message)
//
//        val errorMessage = ErrorMessage(
//            HttpStatus.NOT_ACCEPTABLE.value(),
//            errors.toString()
//        )
//        return ResponseEntity(errorMessage, HttpStatus.NOT_ACCEPTABLE)
//
//    }

}