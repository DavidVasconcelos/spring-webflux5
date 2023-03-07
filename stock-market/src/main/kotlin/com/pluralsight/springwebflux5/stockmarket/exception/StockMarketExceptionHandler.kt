package com.pluralsight.springwebflux5.stockmarket.exception

import com.pluralsight.springwebflux5.stockcommons.model.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class StockMarketExceptionHandler {

    @ExceptionHandler(StockMarketPublishingException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleStockPublishingException(ex: StockMarketPublishingException) = ErrorMessage(ex.message)
}
