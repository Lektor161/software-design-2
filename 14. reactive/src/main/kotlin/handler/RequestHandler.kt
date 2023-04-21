package handler

import dao.ProductDAO
import dao.UserDAO
import io.netty.handler.codec.http.HttpMethod
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import model.Currency
import model.Product
import model.User
import rx.Observable

class RequestHandler(
    private val catalogDAO: ProductDAO,
    private val userDAO: UserDAO,
) {
    fun handleRequest(req: HttpServerRequest<*>): Observable<String> {
        val params = req.queryParameters
        try {
            when {
                req.decodedPath == "/addUser" && req.httpMethod == HttpMethod.GET -> {
                    return handleNewUser(params)
                }
                req.decodedPath == "/user" && req.httpMethod == HttpMethod.GET -> {
                    return handleGetUser(params)
                }

                req.decodedPath == "/addProduct" && req.httpMethod == HttpMethod.GET -> {
                    return handleNewProduct(params)
                }
                req.decodedPath == "/products" && req.httpMethod == HttpMethod.GET -> {
                    return handleListProducts(params)
                }
            }
        } catch (runtimeException: RuntimeException) {
            return Observable.just(runtimeException.toString())
        }


        return Observable.just("there isn't page[${req.decodedPath}]")
    }

    private fun handleListProducts(params: MutableMap<String, MutableList<String>>) =
        catalogDAO.listAll(params["userId"]!![0]).map { it.toString() }

    private fun handleNewProduct(params: MutableMap<String, MutableList<String>>) =
        catalogDAO.createProduct(
            Product(
                getParam(params, "name"),
                getParam(params, "price").toDouble(),
                Currency.parse(getParam(params, "currency"))
            )
        ).map { it.toString() }

    private fun handleGetUser(params: Map<String, MutableList<String>>) =
        userDAO.getUser(
            getParam(params, "id")
        ).map { it.toString() }

    private fun handleNewUser(params: MutableMap<String, MutableList<String>>) =
        userDAO.createUser(
            User(
                getParam(params, "id"),
                getParam(params, "name"),
                getParam(params, "login"),
                Currency.parse(getParam(params, "currency"))
            )
        ).map { it.toString() }

    private fun getParam(params: Map<String, MutableList<String>>, param: String): String {
        return params[param]?.get(0) ?: throw RuntimeException("there isn't $param")
    }
}