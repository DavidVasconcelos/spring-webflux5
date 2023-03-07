package com.pluralsight.springwebflux5.stockmarket.service

import com.pluralsight.springwebflux5.stockcommons.dto.StockPublishRequest
import com.pluralsight.springwebflux5.stockcommons.model.Currency
import com.pluralsight.springwebflux5.stockcommons.model.StockMarketStatus
import com.pluralsight.springwebflux5.stockmarket.entity.StockMarket
import com.pluralsight.springwebflux5.stockmarket.exception.StockMarketPublishingException
import com.pluralsight.springwebflux5.stockmarket.repository.StocksMarketRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class StockPublishingService(private val repository: StocksMarketRepository) {

    fun publishStock(request: StockPublishRequest) = Mono.just(request)
        .map { request ->
            StockMarket(
                stockName = request.stockName,
                price = request.price,
                currencyName = Currency.getCurrency(request.currencyName).toString(),
                status = getStatus(request.stockName),
                dateTime = LocalDateTime.now()
            )
        }
        .flatMap { stockMarket -> repository.save(stockMarket) }
        .map(StockMarket::toResponse)
        .onErrorMap { ex -> StockMarketPublishingException(ex.message!!) }

    fun getStatus(stockName: String): String {
        return if (stockName.contains(INVALID_STOCK_NAME_CHARACTER)) {
            throw StockMarketPublishingException("Stock name with illegal character '$INVALID_STOCK_NAME_CHARACTER'")
        } else {
            StockMarketStatus.SUCCESS.toString()
        }
    }

    companion object {
        const val INVALID_STOCK_NAME_CHARACTER = "-"
    }
}
