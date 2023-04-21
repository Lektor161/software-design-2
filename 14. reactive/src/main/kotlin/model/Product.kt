package model

import org.bson.Document

data class Product(
    val name: String,
    val price: Double,
    val currency: Currency,
) {

    constructor(document: Document): this(
        document.getString("name"),
        document.getString("price").toDouble(),
        Currency.parse(document.getString("id"))
    )

    fun changeCurrency(currency: Currency): Product {
        val CURRENCY_TO_RUB = mapOf(
            Currency.RUB to 1.0,
            Currency.USD to 80.0,
            Currency.EUR to 90.0,
        )
        return Product(
            name,
            price * CURRENCY_TO_RUB[this.currency]!! / CURRENCY_TO_RUB[currency]!!,
            currency
        )
    }

    fun toDocument() = Document(
        mapOf(
            "name" to name,
            "price" to price,
            "currency" to currency,
        )
    )

    override fun toString(): String {
        return """
            Product {
                name     = $name
                price    = $price
                currency = $currency
            }
        """.trimIndent()
    }
}