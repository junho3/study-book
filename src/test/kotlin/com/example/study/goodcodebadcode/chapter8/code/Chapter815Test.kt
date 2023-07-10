package com.example.study.goodcodebadcode.chapter8.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class Chapter815Test(
    private val chapter815: Chapter815,
) : DescribeSpec({

    describe("close 메소드는") {
        context("메소드가 호출되면") {
            it("true를 리턴한다.") {
                val result = chapter815.close()

                result shouldBe true
            }
        }
    }
})
