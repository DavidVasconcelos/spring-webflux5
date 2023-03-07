package com.pluralsight.springwebflux5.stocktrading.service

import com.pluralsight.springwebflux5.stockcommons.dto.StockRequest
import com.pluralsight.springwebflux5.stockcommons.dto.StockResponse
import com.pluralsight.springwebflux5.stocktrading.entity.Stock
import com.pluralsight.springwebflux5.stocktrading.exception.StockCreationException
import com.pluralsight.springwebflux5.stocktrading.exception.StockNotFoundException
import com.pluralsight.springwebflux5.stocktrading.repository.StocksRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal

@Service
class StocksService(private val repository: StocksRepository) {
    fun getOneStock(id: String): Mono<StockResponse> = repository
        .findById(id)
        .map(Stock::toResponse)
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

    fun createStock(stockRequest: StockRequest) = Mono.just(stockRequest)
        .map(::Stock)
        .flatMap { stock -> repository.save(stock) }
        .map(Stock::toResponse)
        .onErrorMap { ex -> StockCreationException(ex.message!!) }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}