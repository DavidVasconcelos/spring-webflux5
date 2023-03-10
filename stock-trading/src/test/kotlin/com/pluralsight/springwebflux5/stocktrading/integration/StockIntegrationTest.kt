package com.pluralsight.springwebflux5.stocktrading.integration

import com.ninjasquad.springmockk.MockkBean
import com.pluralsight.springwebflux5.stockcommons.dto.StockResponse
import com.pluralsight.springwebflux5.stockcommons.model.ErrorMessage
import com.pluralsight.springwebflux5.stocktrading.client.StockMarketClient
import com.pluralsight.springwebflux5.stocktrading.mock.MockTest
import com.pluralsight.springwebflux5.stocktrading.mock.MockTest.STOCK_ID
import com.pluralsight.springwebflux5.stocktrading.mock.MockTest.currencyRate
import com.pluralsight.springwebflux5.stocktrading.mock.MockTest.stock
import com.pluralsight.springwebflux5.stocktrading.repository.StocksRepository
import io.mockk.every
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StockIntegrationTest {

    @MockkBean
    private lateinit var repository: StocksRepository

    @MockkBean
    private lateinit var client: StockMarketClient

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun `should get one stock`() {
        every { repository.findById(STOCK_ID) } returns Mono.just(stock)
        every { client.getCurrencyRates(any(), any()) } returns Mono.just(currencyRate)

        val stockResponse = webClient.get()
            .uri { uriBuilder -> uriBuilder.path("/stocks/{id}").build(STOCK_ID) }
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(StockResponse::class.java)
            .returnResult()
            .responseBody

        stockResponse shouldNotBe null
        stockResponse?.id shouldBeEqualTo STOCK_ID
        stockResponse?.name shouldBeEqualTo MockTest.STOCK_NAME
        stockResponse?.price shouldBeEqualTo MockTest.STOCK_PRICE
        stockResponse?.currency shouldBeEqualTo MockTest.STOCK_CURRENCY
    }

    @Test
    fun `should return not found exception when get one stock`() {
        every { repository.findById(STOCK_ID) } returns Mono.empty()

        val errorMessage = webClient.get()
            .uri { uriBuilder -> uriBuilder.path("/stocks/{id}").build(STOCK_ID) }
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody(ErrorMessage::class.java)
            .returnResult()
            .responseBody

        errorMessage?.message?.shouldContain("Stock not found")
    }
}
