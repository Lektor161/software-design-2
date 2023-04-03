package stocks

import io.ktor.server.plugins.*
class StocksServiceImpl: StocksService {
    private val stocks: MutableMap<String, Stock> = mutableMapOf()

    override fun addStock(ticker: String, price: Long, count: Long) {
        if (stocks.containsKey(ticker)) {
            throw BadRequestException("Stock with ticker $ticker already exists")
        }
        stocks[ticker] = Stock(ticker, price, count)
    }

    override fun getStock(ticker: String): StockInfo? =
        stocks[ticker]?.toStockInfo()


    override fun buyStock(ticker: String, count: Long, price: Long): Long =
        getStockInfo(ticker).buy(count, price)

    override fun sellStock(ticker: String, count: Long, price: Long): Long =
        getStockInfo(ticker).sell(count, price)

    override fun changePrice(ticker: String, newPrice: Long) =
        getStockInfo(ticker).changePrice(newPrice)

    private fun getStockInfo(ticker: String) =
        stocks[ticker] ?: throw BadRequestException("Stock with ticker $ticker not found")
}

