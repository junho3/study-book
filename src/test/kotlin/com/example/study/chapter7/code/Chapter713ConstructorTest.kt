package com.example.study.chapter7.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class Chapter713ConstructorTest(
    private val chapter713Constructor: Chapter713Constructor = Chapter713Constructor(),
) : DescribeSpec({

    describe("createStudentByMainConstructor 메소드는") {
        context("id, name, age, score 파라미를 넘겼을 때") {
            val id = 1
            val name = "홍길동"
            val age = 10
            val score = 100

            it("주 생성자로 생성된 Student 객체를 리턴한다.") {
                val result = chapter713Constructor.createStudent(id, name, age, score)

                result.id shouldBe id
                result.name shouldBe name
                result.age shouldBe age
                result.score shouldBe score
            }
        }

        context("id, name, age 파라미를 넘겼을 때") {
            val id = 1
            val name = "홍길동"
            val age = 10

            it("score가 null인 주 생성자로 생성된 Student 객체를 리턴한다.") {
                val result = chapter713Constructor.createStudent(id, name, age)

                result.id shouldBe id
                result.name shouldBe name
                result.age shouldBe age
                result.score.shouldBeNull()
            }
        }

        context("id, name 파라미터를 넘겼을 때") {
            val id = 1
            val name = "홍길동"

            it("age가 8이고, score가 null인 부 생성자로 생성된 Student 객체를 리턴한다.") {
                val result = chapter713Constructor.createStudent(id, name)

                result.id shouldBe id
                result.name shouldBe name
                result.age shouldBe 8
                result.score.shouldBeNull()
            }
        }
    }
})
