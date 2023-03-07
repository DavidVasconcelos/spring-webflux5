package com.pluralsight.springwebflux5.stockcommons.dto

import java.math.BigDecimal

data class StockPublishResponse(
    val stockName: String,
    val price: BigDecimal,
    val currencyName: String,
    val status: String
)
