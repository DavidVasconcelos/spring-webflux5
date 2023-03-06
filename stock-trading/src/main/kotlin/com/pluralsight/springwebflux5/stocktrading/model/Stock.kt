package com.pluralsight.springwebflux5.stocktrading.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document
data class Stock(
    @Id val id: String? = null,
    val name: String,
    val price: BigDecimal,
    val currency: Currency
)
