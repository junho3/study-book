package com.example.study.goodcodebadcode.chapter7

import com.example.study.goodcodebadcode.chapter7.Chapter713StaticFactoryMethod
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class Chapter713StaticFactoryMethodTest(
    private val chapter713StaticFactoryMethod: Chapter713StaticFactoryMethod = Chapter713StaticFactoryMethod(),
) : DescribeSpec({

    describe("createStudent 메소드는") {
        context("id, name, age, score 파라미터를 넘겼을 때") {
            val id = 1
            val name = "홍길동"
            val age = 10
            val score = 100

            it("Student 객체를 리턴한다.") {
                val result = chapter713StaticFactoryMethod.createStudent(id, name, age, score)

                result.id shouldBe id
                result.name shouldBe name
                result.age shouldBe age
                result.score shouldBe score
            }
        }

        context("id, name 파라미터를 넘겼을 때") {
            val id = 1
            val name = "홍길동"
            it("age가 8이고, score가 null인 Student 객체를 리턴한다.") {
                val result = chapter713StaticFactoryMethod.createStudent(id, name)

                result.id shouldBe id
                result.name shouldBe name
                result.age shouldBe 8
                result.score.shouldBeNull()
            }
        }
    }
})
