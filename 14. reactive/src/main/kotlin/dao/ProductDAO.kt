package dao

import com.mongodb.rx.client.MongoDatabase
import model.Currency
import model.Product
import org.bson.Document
import rx.Observable
import java.lang.RuntimeException

class ProductDAO(private val mongoDatabase: MongoDatabase, private val userDAO: UserDAO) {
    fun createProduct(product: Product): Observable<Boolean> =
        mongoDatabase.getCollection(PRODUCT_COLLECTION).insertOne(product.toDocument())
            .asObservable().isEmpty.map { !it }

    fun listAll(userId: String): Observable<String> {
        val user = userDAO.getUser(userId)
        return user.isEmpty.flatMap { userDoesNotExist ->
            if (userDoesNotExist) {
                return@flatMap Observable.just("User not found")
            } else {
                return@flatMap user.flatMap { user ->
                    mongoDatabase.getCollection(PRODUCT_COLLECTION).find().toObservable()
                        .map { Product(it).changeCurrency(user.currency).toString() }
                }
            }
        }
    }

    companion object {
        const val PRODUCT_COLLECTION = "product-collection"
    }
}