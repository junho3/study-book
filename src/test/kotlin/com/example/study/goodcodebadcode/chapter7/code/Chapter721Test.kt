package com.example.study.goodcodebadcode.chapter7.code

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

    describe("addFont 메소드는") {
        context("name이 주어졌을 때") {
            val name = "기울림체"
            it("폰트를 추가한다.") {
                val result = chapter721.addFont(name)

                result.fonts.size shouldBe 3
                result.fonts.last().name shouldBe name
            }
        }
    }
})
