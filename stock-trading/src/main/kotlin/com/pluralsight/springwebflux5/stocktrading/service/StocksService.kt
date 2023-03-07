package com.pluralsight.springwebflux5.stocktrading.service

import com.pluralsight.springwebflux5.stockcommons.dto.StockPublishRequest
import com.pluralsight.springwebflux5.stockcommons.dto.StockRequest
import com.pluralsight.springwebflux5.stockcommons.dto.StockResponse
import com.pluralsight.springwebflux5.stockcommons.model.StockMarketStatus
import com.pluralsight.springwebflux5.stocktrading.client.StockMarketClient
import com.pluralsight.springwebflux5.stocktrading.entity.Stock
import com.pluralsight.springwebflux5.stocktrading.exception.StockCreationException
import com.pluralsight.springwebflux5.stocktrading.exception.StockNotFoundException
import com.pluralsight.springwebflux5.stocktrading.repository.StocksRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class StocksService(
    private val repository: StocksRepository,
    private val marketClient: StockMarketClient
) {
    fun getOneStock(id: String, currency: String?): Mono<StockResponse> = repository
        .findById(id)
        .flatMap { stock -> mapToStockResponse(currency, stock) }
        .switchIfEmpty(Mono.error(StockNotFoundException("Stock not found with id: $id")))
        .doFirst { run { logger.info("Retrieving stock with id: $id") } }
        .doOnNext { stock -> logger.info("Stock found: $stock") }
        .doOnError { ex -> logger.error("Something went wrong while retrieving the stock with id: $id", ex) }
        .doOnTerminate { run { logger.info("Finalized retrieving stock") } }
        .doFinally { signalType -> logger.info("Finalized retrieving stock with signal type: $signalType") }

    fun getAllStocks(priceGreaterThan: BigDecimal): Flux<StockResponse> = repository
        .findAll()
        .filter { stock -> stock.price > priceGreaterThan }
        .map(Stock::toResponse)
        .doFirst { run { logger.info("Retrieving all stocks") } }
        .doOnNext { stock -> logger.info("Stock found: $stock") }
        .doOnError { ex -> logger.error("Something went wrong while retrieving the stocks", ex) }
        .doOnTerminate { run { logger.info("Finalized retrieving stocks") } }
        .doFinally { signalType -> logger.info("Finalized retrieving stock with signal type: $signalType") }

    @Transactional
    fun createStock(stockRequest: StockRequest) = Mono.just(stockRequest)
        .map(::Stock)
        .flatMap { stock -> repository.save(stock) }
        .flatMap { stock ->
            publishToMarket(stockRequest)
                .map { stock.toResponse() }
        }
        .onErrorMap { ex -> StockCreationException(ex.message!!) }

    private fun mapToStockResponse(
        currencyTo: String?,
        stock: Stock
    ): Mono<StockResponse> {
        return currencyTo?.let {
            getCurrencyRate(currencyFrom = stock.currency.toString(), currencyTo = it)
                .map { currencyRate ->
                    StockResponse(
                        id = stock.id!!,
                        name = stock.name,
                        currency = currencyRate.currencyName,
                        price = (stock.price.multiply(currencyRate.rate).setScale(2, RoundingMode.CEILING))
                    )
                }
        } ?: Mono.just(
            StockResponse(
                id = stock.id!!,
                name = stock.name,
                currency = stock.currency.toString(),
                price = stock.price
            )
        )
    }

    private fun getCurrencyRate(currencyFrom: String, currencyTo: String) = marketClient
        .getCurrencyRates(
            currencyFrom = currencyFrom,
            currencyTo = currencyTo
        )

    private fun publishToMarket(stockRequest: StockRequest) =
        marketClient.publishStock(StockPublishRequest(stockRequest))
            .filter { stockResponse -> stockResponse.status == StockMarketStatus.SUCCESS.toString() }
            .switchIfEmpty(Mono.error(StockCreationException("Unable to publish stock to the Stock Market")))

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
