package com.example.study.goodcodebadcode.chapter5

import com.example.study.goodcodebadcode.chapter5.Chapter554
import com.example.study.goodcodebadcode.chapter5.Vehicle
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class Chapter554Test(
    private val chapter554: Chapter554 = Chapter554(),
) : DescribeSpec({

    describe("sendOwnerALetter 메소드는") {
        context("scraped가 true인 Vehicle 객체가 주어졌을 때") {
            val vehicle = Vehicle(true)
            it("String 타입의 SEND_LETTER를 리턴한다.") {
                val result = chapter554.sendOwnerALetter(vehicle)

                result shouldBe "SEND_LETTER"
            }
        }

        context("scraped가 false인 Vehicle 객체가 주어졌을 때") {
            val vehicle = Vehicle(false)
            it("null을 리턴한다.") {
                val result = chapter554.sendOwnerALetter(vehicle)

                result.shouldBeNull()
            }
        }
    }
})
