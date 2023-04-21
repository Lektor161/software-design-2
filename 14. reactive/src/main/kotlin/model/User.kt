package model

import org.bson.Document

data class User(
    val id: String,
    val name: String,
    val login: String,
    val currency: Currency,
) {

    constructor(document: Document): this(
        document.getString("id"),
        document.getString("id"),
        document.getString("id"),
        Currency.parse(document.getString("id"))
    )

    fun toDocument() = Document(
        mapOf(
            "id" to id,
            "name" to name,
            "login" to login,
            "currency" to currency,
        )
    )

    override fun toString(): String {
        return """
            User {
                id       = $id
                name     = $name
                login    = $login
                currency = $currency
            }
        """.trimIndent()
    }
}