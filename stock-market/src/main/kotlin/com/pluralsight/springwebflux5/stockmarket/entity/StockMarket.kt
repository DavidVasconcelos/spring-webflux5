package com.pluralsight.springwebflux5.stockmarket.entity

import com.pluralsight.springwebflux5.stockcommons.dto.StockPublishResponse
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document
data class StockMarket(
    @Id val id: String? = null,
    val stockName: String,
    val price: BigDecimal,
    val currencyName: String,
    val status: String,
    val dateTime: LocalDateTime
) {
    fun toResponse() = StockPublishResponse(
        stockName = this.stockName,
        price = this.price,
        currencyName = this.currencyName,
        status = this.status
    )
}
