package com.pluralsight.springwebflux5.stocktrading.service

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import com.pluralsight.springwebflux5.stocktrading.client.StockMarketClient
import com.pluralsight.springwebflux5.stocktrading.exception.StockCreationException
import com.pluralsight.springwebflux5.stocktrading.mock.MockTest.STOCK_CURRENCY
import com.pluralsight.springwebflux5.stocktrading.mock.MockTest.STOCK_ID
import com.pluralsight.springwebflux5.stocktrading.mock.MockTest.STOCK_NAME
import com.pluralsight.springwebflux5.stocktrading.mock.MockTest.STOCK_PRICE
import com.pluralsight.springwebflux5.stocktrading.mock.MockTest.stock
import com.pluralsight.springwebflux5.stocktrading.mock.MockTest.stockPublishResponseFailureStatus
import com.pluralsight.springwebflux5.stocktrading.mock.MockTest.stockPublishResponseSuccessStatus
import com.pluralsight.springwebflux5.stocktrading.mock.MockTest.stockRequest
import com.pluralsight.springwebflux5.stocktrading.repository.StocksRepository
import io.mockk.every
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(SpringExtension::class)
class StockServiceTest {

    @SpykBean
    private lateinit var service: StocksService

    @MockkBean
    private lateinit var repository: StocksRepository

    @MockkBean
    private lateinit var client: StockMarketClient

    @Test
    fun `should create stock`() {
        every { repository.save(any()) } returns Mono.just(stock)
        every { client.publishStock(any()) } returns Mono.just(stockPublishResponseSuccessStatus)

        StepVerifier
            .create(service.createStock(stockRequest))
            .assertNext { stockResponse -> // because on next
                stockResponse shouldNotBe null
                stockResponse.id shouldBeEqualTo STOCK_ID
                stockResponse.name shouldBeEqualTo STOCK_NAME
                stockResponse.price shouldBeEqualTo STOCK_PRICE
                stockResponse.currency shouldBeEqualTo STOCK_CURRENCY
            }
            .verifyComplete()
    }

    @Test
    fun `should throw StockCreationException when unable to save`() {
        every { repository.save(any()) } throws RuntimeException("Connection Lost")

        StepVerifier
            .create(service.createStock(stockRequest))
            .verifyError(StockCreationException::class.java)
    }

    @Test
    fun `should throw StockCreationException when stockMarket failed`() {
        every { repository.save(any()) } returns Mono.just(stock)
        every { client.publishStock(any()) } returns Mono.just(stockPublishResponseFailureStatus)

        StepVerifier
            .create(service.createStock(stockRequest))
            .verifyError(StockCreationException::class.java)
    }
}
