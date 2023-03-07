package com.pluralsight.springwebflux5.stockmarket.service

import com.pluralsight.springwebflux5.stockcommons.model.Currency
import com.pluralsight.springwebflux5.stockcommons.model.Currency.BRL
import com.pluralsight.springwebflux5.stockcommons.model.Currency.EUR
import com.pluralsight.springwebflux5.stockcommons.model.Currency.USD
import com.pluralsight.springwebflux5.stockcommons.model.CurrencyRate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.*

@Service
class CurrencyRatesService {

    private val currencyRateProviderMap: MutableMap<Pair<Currency, Currency>, BigDecimal> =
        Collections.synchronizedMap(HashMap())

    init {
        currencyRateProviderMap[Pair(USD, BRL)] = BigDecimal.valueOf(USD_TO_BRL)
        currencyRateProviderMap[Pair(USD, EUR)] = BigDecimal.valueOf(USD_TO_EUR)
        currencyRateProviderMap[Pair(BRL, USD)] = BigDecimal.valueOf(BRL_TO_USD)
        currencyRateProviderMap[Pair(BRL, EUR)] = BigDecimal.valueOf(BRL_TO_EUR)
        currencyRateProviderMap[Pair(EUR, USD)] = BigDecimal.valueOf(EUR_TO_USD)
        currencyRateProviderMap[Pair(EUR, BRL)] = BigDecimal.valueOf(EUR_TO_BRL)
    }

    fun getCurrencyRates(currencyFrom: Currency, currencyTo: Currency): Mono<CurrencyRate> {
        val rate = if (currencyTo == currencyFrom) {
            BigDecimal.ONE
        } else {
            currencyRateProviderMap[Pair(currencyFrom, currencyTo)]!!
        }
        return Mono.just(
            CurrencyRate(
                currencyName = currencyTo.toString(),
                rate = rate
            )
        )
    }

    companion object {
        const val USD_TO_BRL = 5.19
        const val USD_TO_EUR = 0.95
        const val BRL_TO_USD = 0.19
        const val BRL_TO_EUR = 0.18
        const val EUR_TO_USD = 1.06
        const val EUR_TO_BRL = 5.48
    }
}
