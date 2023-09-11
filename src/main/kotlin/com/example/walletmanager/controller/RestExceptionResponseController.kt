package com.example.walletmanager.controller

import com.example.walletmanager.controller.exception.InvalidUsernameException
import com.example.walletmanager.controller.exception.TransactionValidationException
import com.example.walletmanager.controller.exception.UserNotFoundException
import com.example.walletmanager.model.response.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class RestExceptionResponseController: ResponseEntityExceptionHandler() {
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
        return ResponseEntity(errorMessage, HttpStatus.NOT_ACCEPTABLE)
    }
//
//    @ExceptionHandler(value = [InvalidUsernameException::class, UserNotFoundException::class])
//    fun handleRenamingException(ex: InvalidUsernameException, ex2: UserNotFoundException) : ResponseEntity<ErrorMessage> {
//        val errors = arrayOf(ex.message, ex2.message)
//
//        val errorMessage = ErrorMessage(
//            HttpStatus.NOT_ACCEPTABLE.value(),
//            "Teeesttt"
//        )
//        return ResponseEntity(errorMessage, HttpStatus.NOT_ACCEPTABLE)
//
//    }

//
//    @ExceptionHandler
//    fun handleDefaultException() : ResponseEntity<ErrorMessage> {
//        val errorMessage = ErrorMessage(
//            HttpStatus.NOT_ACCEPTABLE.value(),
//            "Internal Error! Please try again later!"
//        )
//        return ResponseEntity(errorMessage, HttpStatus.NOT_ACCEPTABLE)
//    }

}