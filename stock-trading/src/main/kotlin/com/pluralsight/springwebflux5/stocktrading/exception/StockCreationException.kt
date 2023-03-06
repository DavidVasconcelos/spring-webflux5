package com.pluralsight.springwebflux5.stocktrading.exception

data class StockCreationException(override val message: String) : RuntimeException(message)
