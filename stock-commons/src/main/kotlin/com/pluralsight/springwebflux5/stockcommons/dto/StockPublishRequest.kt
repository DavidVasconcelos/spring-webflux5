package com.pluralsight.springwebflux5.stockcommons.dto

import java.math.BigDecimal

data class StockPublishRequest(
    val stockName: String,
    val price: BigDecimal,
    val currencyName: String
) {
    constructor(stockRequest: StockRequest) : this(
        stockName = stockRequest.name,
        price = stockRequest.price,
        currencyName = stockRequest.currency
    )
}
