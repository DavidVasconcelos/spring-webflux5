package com.pluralsight.springwebflux5.stockcommons.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class StockResponse(
    val id: String? = null,
    @JsonProperty("stockName") val name: String? = null,
    val price: BigDecimal? = null,
    val currency: String? = null
)
