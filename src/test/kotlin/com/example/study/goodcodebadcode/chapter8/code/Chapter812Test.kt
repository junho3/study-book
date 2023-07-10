package com.example.study.goodcodebadcode.chapter8.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class Chapter812Test(
    private val chapter812: Chapter812,
) : DescribeSpec({

    describe("create 메소드는") {
        context("KAKAOPAY가 주어지면") {
            val paymentGateway = Chapter812.PaymentGateway.KAKAOPAY
            it("카카오페이를 리턴한다.") {
                val result = chapter812.create(paymentGateway)

                result.name shouldBe "카카오페이"
            }
        }

        context("NAVERPAY가 주어지면") {
            val paymentGateway = Chapter812.PaymentGateway.NAVERPAY
            it("네이버페이를 리턴한다.") {
                val result = chapter812.create(paymentGateway)

                result.name shouldBe "네이버페이"
            }
        }
    }
})
