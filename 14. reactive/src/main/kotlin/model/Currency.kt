package model

import java.lang.RuntimeException

enum class Currency {
    USD,
    EUR,
    RUB;

    companion object {
        fun parse(currency: String): Currency {
            return when(currency) {
                "usd" -> USD
                "eur" -> EUR
                "rub" -> RUB
                else -> throw RuntimeException("unknown currency[$currency]")
            }
        }
    }
}