package com.example.study.goodcodebadcode.chapter4

import com.example.study.goodcodebadcode.chapter4.Chapter4ResultTest
import com.example.study.goodcodebadcode.chapter4.PaymentApiResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.types.shouldBeInstanceOf

class Chapter4Test(
    private val chapter4ResultTest: Chapter4ResultTest = Chapter4ResultTest(),
) : DescribeSpec({

    describe("main 메소드는") {
        context("transactionAmount가 양수이면") {
            val transactionAmount: Long = 1000
            it("PaymentApiResponse를 리턴한다.") {
                val result = chapter4ResultTest.main(transactionAmount)

                result.shouldBeInstanceOf<PaymentApiResponse>()
            }
        }

        context("transactionAmount가 음수이면") {
            val transactionAmount: Long = -1000
            it("IllegalArgumentException를 던진다.") {
                shouldThrow<IllegalArgumentException> { chapter4ResultTest.main(transactionAmount) }
            }
        }
    }
})
