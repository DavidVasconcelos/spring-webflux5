package com.pluralsight.springwebflux5.stockcommons.model

import java.util.*

enum class Currency {
    USD,
    BRL,
    EUR;

    companion object {
        fun getCurrency(currency: String) =
            values().firstOrNull() { it.name == currency.trim().uppercase(Locale.getDefault()) } ?: USD
    }
}
