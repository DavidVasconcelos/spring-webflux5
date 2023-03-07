package com.pluralsight.springwebflux5.stocktrading.client

import com.pluralsight.springwebflux5.stockcommons.dto.StockPublishRequest
import com.pluralsight.springwebflux5.stockcommons.dto.StockPublishResponse
import com.pluralsight.springwebflux5.stockcommons.model.CurrencyRate
import com.pluralsight.springwebflux5.stocktrading.exception.StockCreationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class StockMarketClient(@Value("\${clients.stockMarket.baseUrl}") private val baseUrl: String) {

    private val webClient: WebClient =
        WebClient.builder().baseUrl(baseUrl)
            .filter(
                ExchangeFilterFunction.ofRequestProcessor { request ->
                    Mono.just(
                        ClientRequest.from(request)
                            .header("X-Trace-Id", UUID.randomUUID().toString())
                            .build()
                    )
                }
            ).build()

    fun getCurrencyRates(currencyFrom: String, currencyTo: String): Mono<CurrencyRate> = webClient.get()
        .uri("/currencyRates?currencyFrom=$currencyFrom&currencyTo=$currencyTo")
        .retrieve()
        .bodyToMono(CurrencyRate::class.java)
        .doFirst { run { logger.info("Calling GET Currency Rates API") } }
        .doOnNext { cr -> logger.info("GET Currency Rates API Response: $cr") }

    fun publishStock(request: StockPublishRequest): Mono<StockPublishResponse> = webClient.post()
        .uri("/stocks/publish")
        .body(BodyInserters.fromValue(request))
        .exchangeToMono { response -> mapToResponse(response) }
        .doFirst { run { logger.info("Calling Publish Stock API with Request Body: $request") } }
        .doOnNext { spr -> logger.info("Publish Stock API Response: $spr") }

    private fun mapToResponse(response: ClientResponse) =
        if (!response.statusCode().isError) {
            response.bodyToMono(StockPublishResponse::class.java)
        } else {
            response.bodyToMono(StockCreationException::class.java)
                .flatMap { exception -> Mono.error(exception) }
        }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
