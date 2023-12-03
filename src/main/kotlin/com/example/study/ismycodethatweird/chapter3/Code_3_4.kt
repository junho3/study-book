package com.example.study.ismycodethatweird.chapter3

import java.util.Currency

class Code_3_4 {
    
    class Money(
        var amount: Int,
        val currency: Currency,
    ) {
        
        fun add(other: Int) {
            amount += other
        }
        
        init {
            require(amount > 0) { "금액은 0 이상의 값을 지정해주세요." }
        }
    }
}
