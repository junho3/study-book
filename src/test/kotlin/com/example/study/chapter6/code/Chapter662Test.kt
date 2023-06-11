package com.example.study.chapter6.code

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class Chapter662Test(
    private val chapter662: Chapter662 = Chapter662(),
) : DescribeSpec({

    describe("getAnimalSound 메소드는") {
        context("DOG가 주어지면") {
            val animal = Chapter662.Animal.DOG
            it("왈왈 울음소리를 리턴한다.") {
                val result = chapter662.getAnimalSound(animal)

                result shouldBe "왈왈"
            }
        }

        context("FISH가 주어지면") {
            val animal = Chapter662.Animal.FISH
            it("IllegalStateException 익셉션을 던진다.") {
                shouldThrow<IllegalStateException> { chapter662.getAnimalSound(animal) }
            }
        }
    }
})
