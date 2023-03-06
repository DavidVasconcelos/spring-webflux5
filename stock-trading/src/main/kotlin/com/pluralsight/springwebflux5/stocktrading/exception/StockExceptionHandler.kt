package com.pluralsight.springwebflux5.stocktrading.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class StockExceptionHandler {

    @ExceptionHandler(StockNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleStockNotFoundException(ex: StockNotFoundException) = ErrorMessage(ex.message)

    @ExceptionHandler(StockCreationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleStockCreationException(ex: StockCreationException) = ErrorMessage(ex.message)
}
