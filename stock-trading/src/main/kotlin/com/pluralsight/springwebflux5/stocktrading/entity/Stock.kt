package com.pluralsight.springwebflux5.stocktrading.entity

import com.pluralsight.springwebflux5.stockcommons.dto.StockRequest
import com.pluralsight.springwebflux5.stockcommons.dto.StockResponse
import com.pluralsight.springwebflux5.stockcommons.model.Currency
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document
data class Stock(
    @Id val id: String? = null,
    val name: String,
    val price: BigDecimal,
    val currency: Currency
) {
    constructor(request: StockRequest) : this(
        request.id,
        request.name,
        request.price,
        Currency.getCurrency(request.currency)
    )

    fun toResponse() = StockResponse(
        id = this.id!!,
        name = this.name,
        price = this.price,
        currency = this.currency.toString()
    )
}
