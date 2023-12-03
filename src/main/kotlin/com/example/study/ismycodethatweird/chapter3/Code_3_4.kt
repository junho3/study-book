package com.example.study.ismycodethatweird.chapter3

import java.util.Currency

class Code_3_4 {
    
    @JvmInline
    value class Amount(val value: Int) {
        operator fun plus(amount: Amount): Amount = Amount(value + amount.value)
    }
    
    class Money(
        var amount: Amount,
        val currency: Currency,
    ) {
        
        fun add(other: Amount) {
            amount += other
        }
        
        fun add2(other: Amount): Money {
            return Money(
                amount = amount + other,
                currency = currency,
            )
        }
        
        init {
            require(amount.value > 0) { "금액은 0 이상의 값을 지정해주세요." }
        }
    }
}
