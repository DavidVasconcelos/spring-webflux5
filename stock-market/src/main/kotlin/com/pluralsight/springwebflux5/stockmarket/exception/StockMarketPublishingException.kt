package com.pluralsight.springwebflux5.stockmarket.exception

data class StockMarketPublishingException(override val message: String) : RuntimeException(message)
