package com.example.study.chapter7.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class Chapter721Test(
    private val chapter721: Chapter721 = Chapter721(),
) : DescribeSpec({

    describe("changeColor 메소드는") {
        context("color가 주어졌을 때") {
            val color = "파란색"
            it("첫번째 폰트의 color를 파란색으로 변경한다.") {
                val result = chapter721.changeColor(color)

                result.fonts.first().color shouldBe color
            }
        }
    }
})
