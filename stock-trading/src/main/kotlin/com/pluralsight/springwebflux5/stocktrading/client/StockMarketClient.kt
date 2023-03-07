package com.pluralsight.springwebflux5.stocktrading.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class StockMarketClient(@Value("\${clients.stockMarket.baseUrl}") private val baseUrl: String) {

    private val webClient: WebClient = WebClient.builder().baseUrl(baseUrl).build()
    fun getCurrencyRates() {
        println("teste")
        logger.info(webClient.toString())
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
