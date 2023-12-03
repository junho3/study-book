package com.example.study.ismycodethatweird.chapter3

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import java.util.Currency

@DisplayName("Code_3_4")
class Code_3_4Test : DescribeSpec({
    
    describe("Money 클래스는") {
        context("amount 프로퍼티가 음수인 경우") {
            
            val amount = Code_3_4.Amount(-100)
            
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
            amount = Code_3_4.Amount(100),
            currency = Currency.getInstance("KRW")
        )
        
        context("other 10이 주어졌을 때") {
            
            val other = Code_3_4.Amount(10)
            
            it("amount는 110이 된다.") {
                money.add(other)
                
                money.amount.value shouldBe 110
            }
        }
    }
    
    describe("add2 메소드는") {
        
        val money = Code_3_4.Money(
            amount = Code_3_4.Amount(100),
            currency = Currency.getInstance("KRW")
        )
        
        context("other 10이 주어졌을 때") {
            
            val other = Code_3_4.Amount(10)
            
            it("amount가 110인 새로운 Moneny 객체를 리턴한다.") {
                val actual = money.add2(other)
                
                actual.amount.value shouldBe 110
                actual shouldNotBeSameInstanceAs money
            }
        }
    }
})
