package com.pluralsight.springwebflux5.stockcommons.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class StockResponse(
    val id: String,
    @JsonProperty("stockName") val name: String,
    val price: BigDecimal,
    val currency: String
)
