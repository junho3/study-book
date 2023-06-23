package com.example.study.chapter8.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class KotlinDelegationTest(
    private val kotlinDelegation: KotlinDelegation = KotlinDelegation(),
) : DescribeSpec({

    describe("main 메소드는") {
        context("파라미터 x를 넘기면") {
            val x = 100
            it("println을 수행하고, x를 리턴한다.") {
                val result = kotlinDelegation.main(x)

                result shouldBe x
            }
        }
    }
})
