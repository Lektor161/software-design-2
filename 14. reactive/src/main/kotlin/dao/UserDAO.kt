package dao

import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoDatabase
import model.User
import org.bson.Document
import rx.Observable

class UserDAO(private val db: MongoDatabase) {
    fun createUser(user: User): Observable<Boolean> =
        db.getCollection(USER_COLLECTION).insertOne(user.toDocument())
            .asObservable().isEmpty.map { !it }

    fun getUser(id: String): Observable<User> =
        db.getCollection(USER_COLLECTION).find(Filters.eq("id", id))
            .toObservable().map { User(it) }

    companion object {
        const val USER_COLLECTION = "user-collection"
    }
}