package com.pluralsight.springwebflux5.stocktrading.controller

import com.pluralsight.springwebflux5.stocktrading.model.Stock
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/stocks")
class StocksController {

    @GetMapping("/{id}")
    fun getOneStock(@PathVariable id: String): Mono<Stock> {
        return Mono.just(Stock(name = "stock-$id"))
    }

    @GetMapping
    fun getAllStocks(): Flux<Stock> {
        return Flux.range(1, 5)
            .map { count -> Stock(name = "stock-$count") }
    }
}