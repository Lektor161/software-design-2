package stocks

interface StocksService {
    suspend fun getStockPrice(ticker: String): Long
    suspend fun getStockInfo(ticker: String): ApiObjects.StockInfoResponse
    suspend fun buyStocks(ticker: String, count: Long, price: Long): Long?
    suspend fun sellStocks(ticker: String, count: Long, price: Long): Long?
}