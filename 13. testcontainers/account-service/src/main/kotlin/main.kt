import account.AccountService
import account.AccountServiceImpl
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import stocks.HttpStocksServiceImpl

object ApiObjects {
    data class CreateUserRequest(val login: String)
    data class TopUpAccountRequest(val amount: Long)
    data class BuyStocksRequest(val ticker: String, val count: Long, val price: Long)
    data class SellStocksRequest(val ticker: String, val count: Long, val price: Long)

    data class GetUserStocksResponseItem(val ticker: String, val count: Long, val price: Long)
}

fun Application.module(accountService: AccountService) {
    routing {
        post("/users") {
            val req: ApiObjects.CreateUserRequest = call.receive()

            accountService.addUser(req.login)
            call.respond(HttpStatusCode.OK)
        }
        post("/users/{login}/balance") {
            val req: ApiObjects.TopUpAccountRequest = call.receive()
            val login = call.parameters["login"] ?: throw BadRequestException("Login not provided")

            accountService.topUpAccount(login, req.amount)
            call.respond(HttpStatusCode.OK)
        }
        get("/users/{login}/stocks") {
            val login = call.parameters["login"] ?: throw BadRequestException("Login not provided")

            val response = accountService.getStocks(login).map {
                ApiObjects.GetUserStocksResponseItem(it.key, it.value.first, it.value.second)
            }

            call.respond(HttpStatusCode.OK, jacksonObjectMapper().writeValueAsString(response))
        }
        put("/users/{login}/stocks") {
            val login = call.parameters["login"] ?: throw BadRequestException("Login not provided")
            val req: ApiObjects.BuyStocksRequest = call.receive()

            accountService.buy(login, req.ticker, req.count)

            call.respond(HttpStatusCode.OK)
        }
        delete("/users/{login}/stocks") {
            val login = call.parameters["login"] ?: throw BadRequestException("Login not provided")
            val req: ApiObjects.SellStocksRequest = call.receive()

            accountService.sell(login, req.ticker, req.count)

            call.respond(HttpStatusCode.OK)
        }
        get("/users/{login}/total") {
            val login = call.parameters["login"] ?: throw BadRequestException("Login not provided")

            val total = accountService.getTotal(login)

            call.respond(HttpStatusCode.OK, total)
        }
    }
}

fun main() {
    val accountService = AccountServiceImpl(HttpStocksServiceImpl(HttpClient(), "http://localhost:8080"))

    embeddedServer(
        Netty,
        port = 8080,
        module = {
            module(accountService)
        }).start(wait = true)
}