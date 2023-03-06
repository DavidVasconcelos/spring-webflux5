package com.pluralsight.springwebflux5.stocktrading.exception

data class StockNotFoundException(override val message: String) : RuntimeException(message)
