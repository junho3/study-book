package com.example.study.goodcodebadcode.chapter6

import com.example.study.goodcodebadcode.chapter6.Chapter662Sealed
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class Chapter662SealedTest(
    private val chapter662Sealed: Chapter662Sealed = Chapter662Sealed(),
) : DescribeSpec({

    describe("findSlipRule 메소드는") {

        context("카카오페이 결제승인 전표룰이 주어지면") {
            val slipRule = Chapter662Sealed.ApproveKakaoPayCardPayment

            it("문자열 타입의 카카오페이_결제승인을 리턴한다.") {
                val result = chapter662Sealed.findSlipRule(slipRule)

                result shouldBe "카카오페이_결제승인"
            }
        }
    }
})
