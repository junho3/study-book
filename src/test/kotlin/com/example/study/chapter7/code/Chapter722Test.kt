package com.example.study.chapter7.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class Chapter722Test(
    private val chapter722: Chapter722 = Chapter722(),
) : DescribeSpec({

    describe("cantChangeColor 메소드는") {
        context("color가 주어졌을 때") {
            val color = "파란색"
            it("첫번째 폰트의 color를 파란색으로 변경하지 못 한다.") {
                val result = chapter722.cantChangeColor(color)

                result.fonts.first().color shouldBe "검정색"
            }
        }
    }

    describe("addFont 메소드는") {
        context("name이 주어졌을 때") {
            val name = "기울림체"
            it("폰트를 추가하지 못 한다.") {
                val result = chapter722.cantAddFont(name)

                result.fonts.size shouldBe 2
                result.fonts.last().name shouldBe "굴림체"
            }
        }
    }
})
