package account

data class Account(val login: String) {
    private val stocks : MutableMap<String, Long> = mutableMapOf()
    private var balance = 0L

    fun upBalance(amount: Long) {
        balance += amount
    }

    fun buyStock(ticker: String, count: Long, price: Long) {
        stocks[ticker] = stocks.getOrDefault(ticker, 0) + count
        balance -= count * price
    }

    fun sellStock(ticker: String, count: Long, price: Long) {
        stocks[ticker] = stocks[ticker]!! - count
        if (stocks[ticker] == 0L) {
            stocks.remove(ticker)
        }
        balance += count * price
    }

    fun getStocks() = stocks

    fun getBalance() = balance
}