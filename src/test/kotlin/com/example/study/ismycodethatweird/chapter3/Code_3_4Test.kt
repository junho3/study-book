package com.example.study.ismycodethatweird.chapter3

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.util.Currency

@DisplayName("Code_3_4")
class Code_3_4Test : DescribeSpec({
    
    describe("Money 클래스는") {
        context("amount 프로퍼티가 음수인 경우") {
            
            val amount = -100
            
            it("IllegalArgumentException을 던진다.") {
                shouldThrow<IllegalArgumentException> { 
                    Code_3_4.Money(
                        amount = amount,
                        currency = Currency.getInstance("KRW")
                    )
                }
            }
        }
    }
    
    describe("add 메소드는") {
        
        val money = Code_3_4.Money(
            amount = 100,
            currency = Currency.getInstance("KRW")
        )
        
        context("other 10이 주어졌을 때") {
            
            val other = 10
            
            it("amount는 110이 된다.") {
                money.add(other)
                
                money.amount shouldBe 110
            }
        }
    }
})
