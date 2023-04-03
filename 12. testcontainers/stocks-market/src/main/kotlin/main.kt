import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import stocks.StocksService
import stocks.StocksServiceImpl

object ApiObjects {
    data class AddStocksRequest(val ticker: String, val price: Long, val count: Long)
    data class BuyStocksRequest(val price: Long, val count: Long)
    data class SellStocksRequest(val price: Long, val count: Long)
    data class ChangePriceRequest(val newPrice: Long)
}

fun Application.module(stockService: StocksService) {
    routing {
        install(ContentNegotiation) {
            json()
        }
        post("/stocks") {
            val req: ApiObjects.AddStocksRequest = jacksonObjectMapper().readValue(
                call.receive<String>(),
                ApiObjects.AddStocksRequest::class.java
            )

            stockService.addStock(req.ticker, req.price, req.count)
            call.respond(HttpStatusCode.OK)
        }
        get("/stock/{ticker}") {
            val ticker = call.parameters["ticker"] ?: throw BadRequestException("Ticker not provided")

            val stockInfo = stockService.getStock(ticker)
            if (stockInfo == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, jacksonObjectMapper().writeValueAsString(stockInfo))
            }
        }
        post("/stock/{ticker}/{buy}") {
            val ticker = call.parameters["ticker"] ?: throw BadRequestException("Ticker not provided")
            val req = jacksonObjectMapper().readValue(call.receive<String>(), ApiObjects.BuyStocksRequest::class.java)

            val price = stockService.buyStock(ticker, req.count, req.price)
            call.respond(HttpStatusCode.OK, jacksonObjectMapper().writeValueAsString(price))
        }
        post("/stock/{ticker}/sell") {
            val ticker = call.parameters["ticker"] ?: throw BadRequestException("Ticker not provided")
            val req = jacksonObjectMapper().readValue(call.receive<String>(), ApiObjects.SellStocksRequest::class.java)

            val price = stockService.sellStock(ticker, req.count, req.price)
            call.respond(HttpStatusCode.OK, jacksonObjectMapper().writeValueAsString(price))
        }
        post("/stock/{ticker}/changePrice") {
            val ticker = call.parameters["ticker"] ?: throw BadRequestException("Ticker not provided")
            val req = jacksonObjectMapper().readValue(call.receive<String>(), ApiObjects.ChangePriceRequest::class.java)

            stockService.changePrice(ticker, req.newPrice)
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun main() {
    val stockService = StocksServiceImpl()

    embeddedServer(
        Netty,
        port = 8085,
        module = {
            module(stockService)
        }).start(wait = true)
}