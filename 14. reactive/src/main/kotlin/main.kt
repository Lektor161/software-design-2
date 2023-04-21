import com.mongodb.rx.client.MongoClients
import dao.ProductDAO
import dao.UserDAO
import handler.RequestHandler
import io.reactivex.netty.protocol.http.server.HttpServer

fun main() {
    val db = MongoClients.create("mongodb://localhost:27017").getDatabase("catalog")

    val userDAO = UserDAO(db)
    val productDAO = ProductDAO(db, userDAO)

    val handler = RequestHandler(productDAO, userDAO)
    HttpServer
        .newServer(8080)
        .start { req, resp ->
            println(req.decodedPath)
            resp.writeString(handler.handleRequest(req))
        }
        .awaitShutdown()
}