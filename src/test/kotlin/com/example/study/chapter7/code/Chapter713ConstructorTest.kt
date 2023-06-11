package com.example.study.chapter7.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class Chapter713ConstructorTest(
    private val chapter713Constructor: Chapter713Constructor = Chapter713Constructor(),
) : DescribeSpec({

    describe("createStudentByMainConstructor 메소드는") {
        context("메소드가 호출된다면") {
            it("주 생성자로 생성된 Student 객체를 리턴한다.") {
                val result = chapter713Constructor.createStudentByMainConstructor()

                result.id shouldBe 1
                result.score shouldBe 100
            }
        }
    }

    describe("createStudentByMainConstructorWithDefaultScore 메소드는") {
        context("메소드가 호출된다면") {
            it("주 생성자로 생성된 Score가 null인 Student 객체를 리턴한다.") {
                val result = chapter713Constructor.createStudentByMainConstructorWithDefaultScore()

                result.id shouldBe 2
                result.score.shouldBeNull()
            }
        }
    }

    describe("createStudentBySubConstructor 메소드는") {
        context("메소드가 호출된다면") {
            it("부 생성자로 생성된 Student 객체를 리턴한다.") {
                val result = chapter713Constructor.createStudentBySubConstructor()

                result.id shouldBe 3
                result.age shouldBe 8
            }
        }
    }
})
