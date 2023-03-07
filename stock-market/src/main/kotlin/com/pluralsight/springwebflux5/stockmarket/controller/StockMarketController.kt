package com.pluralsight.springwebflux5.stockmarket.controller

import com.pluralsight.springwebflux5.stockcommons.dto.StockPublishRequest
import com.pluralsight.springwebflux5.stockcommons.dto.StockPublishResponse
import com.pluralsight.springwebflux5.stockcommons.model.Currency
import com.pluralsight.springwebflux5.stockcommons.model.CurrencyRate
import com.pluralsight.springwebflux5.stockmarket.service.CurrencyRatesService
import com.pluralsight.springwebflux5.stockmarket.service.StockPublishingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class StockMarketController(
    private val currencyRatesService: CurrencyRatesService,
    private val publishingService: StockPublishingService
) {

    @GetMapping("/currencyRates")
    fun getCurrencyRates(
        @RequestHeader(value = "X-Trace-Id", required = false) traceId: String? = null,
        @RequestParam currencyFrom: String,
        @RequestParam currencyTo: String
    ): Mono<CurrencyRate> {
        logger.info("Get Currency Rates API called with Trace Id: $traceId")
        return currencyRatesService.getCurrencyRates(
            currencyFrom = Currency.valueOf(currencyFrom),
            currencyTo = Currency.valueOf(currencyTo)
        )
    }

    @PostMapping("/stocks/publish")
    fun publishStock(
        @RequestHeader(value = "X-Trace-Id", required = false) traceId: String? = null,
        @RequestBody request: StockPublishRequest
    ): Mono<StockPublishResponse> {
        logger.info("Publish Stock API called with Trace Id: $traceId")
        return publishingService.publishStock(request)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
