package account

import io.ktor.server.plugins.*
import stocks.StocksService

class AccountServiceImpl(private val stocksService: StocksService): AccountService {
    private val accounts: MutableMap<String, Account> = mutableMapOf()

    override fun addUser(login: String) {
        if (accounts.containsKey(login)) {
            throw BadRequestException("User already exists")
        }
        accounts[login] = Account(login)
    }

    override fun topUpAccount(login: String, amount: Long) =
        getAccount(login).upBalance(amount)

    override suspend fun getStocks(login: String): Map<String, Pair<Long, Long>>  =
        getAccount(login).getStocks().mapValues {
            it.value to stocksService.getStockPrice(it.key)
        }

    override suspend fun getTotal(login: String): Long =
        getStocks(login).values.sumOf { it.first * it.second } +
                getAccount(login).getBalance()

    override suspend fun buy(login: String, ticker: String, count: Long) {
        val account = getAccount(login)
        while (true) {
            val stockInfo = stocksService.getStockInfo(ticker)
            if (account.getBalance() < stockInfo.price * count) {
                throw BadRequestException("Not enough money")
            }
            if (stockInfo.count < count) {
                throw BadRequestException("Not enough stocks available")
            }

            val price = stocksService.buyStocks(ticker, count, stockInfo.price)
                ?: continue

            account.buyStock(ticker, count, price)
            return
        }
    }

    override suspend fun sell(login: String, ticker: String, count: Long) {
        val account = getAccount(login)
        val allCount = account.getStocks()[ticker]
            ?: throw BadRequestException("User does not have $ticker stocks")

        if (allCount < count) {
            throw BadRequestException("User does not have enough stocks to sell")
        }

        while (true) {
            val stockInfo = stocksService.getStockInfo(ticker)
            val price = stocksService.sellStocks(ticker, count, stockInfo.price)
                ?: continue
            account.sellStock(ticker, count, price)
            return
        }
    }

    private fun getAccount(login: String) =
        accounts[login] ?: throw BadRequestException("User does not exist")
}