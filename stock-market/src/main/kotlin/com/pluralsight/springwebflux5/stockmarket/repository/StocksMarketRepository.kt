package com.pluralsight.springwebflux5.stockmarket.repository

import com.pluralsight.springwebflux5.stockmarket.entity.StockMarket
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface StocksMarketRepository : ReactiveMongoRepository<StockMarket, String>
