package com.pluralsight.springwebflux5.stocktrading.mock

import com.pluralsight.springwebflux5.stockcommons.dto.StockPublishResponse
import com.pluralsight.springwebflux5.stockcommons.dto.StockRequest
import com.pluralsight.springwebflux5.stockcommons.model.Currency
import com.pluralsight.springwebflux5.stockcommons.model.CurrencyRate
import com.pluralsight.springwebflux5.stockcommons.model.StockMarketStatus
import com.pluralsight.springwebflux5.stocktrading.entity.Stock
import java.math.BigDecimal

object MockTest {

    const val STOCK_ID = "621a97f1d11fc40fcdd5c67b"
    const val STOCK_NAME = "Globomantics"
    val STOCK_PRICE: BigDecimal = BigDecimal.TEN
    const val STOCK_CURRENCY = "USD"

    val stockRequest = StockRequest(
        name = STOCK_NAME,
        price = STOCK_PRICE,
        currency = STOCK_CURRENCY
    )
    val stock = Stock(
        id = STOCK_ID,
        name = STOCK_NAME,
        price = STOCK_PRICE,
        currency = Currency.getCurrency(STOCK_CURRENCY)
    )
    val stockPublishResponseSuccessStatus = StockPublishResponse(
        stockName = STOCK_NAME,
        price = STOCK_PRICE,
        currencyName = STOCK_CURRENCY,
        status = StockMarketStatus.SUCCESS.toString()
    )
    val stockPublishResponseFailureStatus = StockPublishResponse(
        stockName = STOCK_NAME,
        price = STOCK_PRICE,
        currencyName = STOCK_CURRENCY,
        status = StockMarketStatus.FAIL.toString()
    )
    val currencyRate = CurrencyRate(
        currencyName = STOCK_CURRENCY,
        rate = BigDecimal.ONE
    )
}
