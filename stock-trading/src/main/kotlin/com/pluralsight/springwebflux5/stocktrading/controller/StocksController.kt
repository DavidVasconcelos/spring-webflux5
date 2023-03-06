package com.pluralsight.springwebflux5.stocktrading.controller

import com.pluralsight.springwebflux5.stocktrading.StocksService
import com.pluralsight.springwebflux5.stocktrading.dto.StockRequest
import com.pluralsight.springwebflux5.stocktrading.dto.StockResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal

@RestController
@RequestMapping("/stocks")
class StocksController(private val service: StocksService) {

    @GetMapping("/{id}")
    fun getOneStock(@PathVariable id: String): Mono<StockResponse> {
        return service.getOneStock(id)
    }

    @GetMapping
    fun getAllStocks(
        @RequestParam(
            required = false,
            defaultValue = "0"
        ) priceGreaterThan: BigDecimal
    ): Flux<StockResponse> {
        return service.getAllStocks(priceGreaterThan)
    }

    @PostMapping
    fun createStock(@RequestBody(required = false) stockRequest: StockRequest): Mono<StockResponse> {
        return service.createStock(stockRequest)
    }
}
