package stocks

import io.ktor.server.plugins.*

class Stock(private val ticker: String, private var price: Long, private var count: Long) {

    fun sell(count: Long, price: Long): Long {
        if (this.price < price) {
            throw BadRequestException("Price of $ticker is ${this.price}, user wanted sell by $price")
        }
        this.count += count
        return this.price
    }

    fun buy(count: Long, price: Long): Long {
        if (this.price > price) {
            throw BadRequestException("Price of $ticker is ${this.price}, user wanted buy by $price")
        }
        if (count > this.count) {
            throw BadRequestException("Not enough stock for $ticker")
        }
        this.count -= count
        return this.price
    }

    fun changePrice(newPrice: Long) {
        price = newPrice
    }

    fun toStockInfo() = StockInfo(price, count)
}