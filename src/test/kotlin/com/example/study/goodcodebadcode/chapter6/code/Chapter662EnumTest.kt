package com.example.study.goodcodebadcode.chapter6.code

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class Chapter662EnumTest(
    private val chapter662Enum: Chapter662Enum = Chapter662Enum(),
) : DescribeSpec({

    describe("getAnimalSound 메소드는") {
        context("DOG가 주어지면") {
            val animal = Chapter662Enum.Animal.DOG
            it("왈왈 울음소리를 리턴한다.") {
                val result = chapter662Enum.getAnimalSound(animal)

                result shouldBe "왈왈"
            }
        }

        context("FISH가 주어지면") {
            val animal = Chapter662Enum.Animal.FISH
            it("IllegalStateException 익셉션을 던진다.") {
                shouldThrow<IllegalStateException> { chapter662Enum.getAnimalSound(animal) }
            }
        }
    }
})
