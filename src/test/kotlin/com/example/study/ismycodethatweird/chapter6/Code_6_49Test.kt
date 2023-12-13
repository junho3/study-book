package com.example.study.ismycodethatweird.chapter6

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

@DisplayName("Code_6_49")
class Code_6_49Test(
    private val goldCustomerPolicy: Code_6_49.GoldCustomerPolicy = Code_6_49.GoldCustomerPolicy()
) : DescribeSpec({
    describe("complyWithAll 메소드는") {
        context("orderHistory가 주어졌을 때") {
            
            val orderHistory = Code_6_49.OrderHistory(
                totalAmount = 100_000_000,
                frequency = 100,
                returnRate = 0.0,
            )
            
            it("true를 리턴한다.") {
                val actual = goldCustomerPolicy.complyWithAll(orderHistory)
                
                actual shouldBe true
            }
        }
    }
})
