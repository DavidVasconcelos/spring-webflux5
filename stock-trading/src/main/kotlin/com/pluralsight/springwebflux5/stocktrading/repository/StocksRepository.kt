package com.pluralsight.springwebflux5.stocktrading.repository

import com.pluralsight.springwebflux5.stocktrading.model.Stock
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface StocksRepository : ReactiveMongoRepository<Stock, String>
