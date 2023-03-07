package com.pluralsight.springwebflux5.stocktrading.client

import com.pluralsight.springwebflux5.stockcommons.model.CurrencyRate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class StockMarketClient(@Value("\${clients.stockMarket.baseUrl}") private val baseUrl: String) {

    private val webClient: WebClient = WebClient.builder().baseUrl(baseUrl).build()
    fun getCurrencyRates(): Flux<CurrencyRate> = webClient.get()
        .uri("/currencyRates")
        .retrieve()
        .bodyToFlux(CurrencyRate::class.java)
        .doFirst { run { logger.info("Calling GET Currency Rates API") } }
        .doOnNext { cr -> logger.info("GET Currency Rates API Response: $cr") }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
