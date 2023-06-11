package com.example.study.chapter6.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class Chapter612Test(
    private val chapter612: Chapter612 = Chapter612(),
) : DescribeSpec({

    describe("getMeanAge 메소드는") {
        context("User 리스트를 받을 경우") {
            val users = listOf(
                Chapter612.User(10),
                Chapter612.User(23),
                Chapter612.User(78),
            )

            it("Double 타입의 평균 나이값을 리턴한다.") {
                val result = chapter612.getMeanAge(users)

                result.shouldBeInstanceOf<Double>()
                result shouldBe 37.0
            }
        }
    }
})
