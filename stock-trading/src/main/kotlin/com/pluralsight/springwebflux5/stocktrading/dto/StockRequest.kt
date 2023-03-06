package com.pluralsight.springwebflux5.stocktrading.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.pluralsight.springwebflux5.stocktrading.model.Currency
import com.pluralsight.springwebflux5.stocktrading.model.Stock
import org.springframework.data.annotation.Id
import java.math.BigDecimal

data class StockRequest(
    @Id val id: String? = null,
    @JsonProperty("stockName") val name: String? = null,
    val price: BigDecimal? = null,
    val currency: String? = null
) {
    fun toModel() = Stock(
        name = this.name!!,
        price = this.price!!,
        currency = Currency.valueOf(this.currency!!)
    )
}
