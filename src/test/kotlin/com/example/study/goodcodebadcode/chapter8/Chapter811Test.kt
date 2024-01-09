package com.example.study.goodcodebadcode.chapter8

import com.example.study.goodcodebadcode.chapter8.Chapter811.Junction
import com.example.study.goodcodebadcode.chapter8.Chapter811.Road
import com.example.study.goodcodebadcode.chapter8.Chapter811.RoutePlanner1
import com.example.study.goodcodebadcode.chapter8.Chapter811.RoutePlanner2
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class Chapter811Test(
    private val routePlanner1: RoutePlanner1 = RoutePlanner1(),
    private val routePlanner2: RoutePlanner2 = RoutePlanner2(),
) : DescribeSpec({
    val startPoint = "xx"
    val endPoint = "xx"

    describe("routePlanner1.planRoute 메소드는") {
        context("startPoint와 endPoint가 주어졌을 때") {
            it("서울의 도로 리스트와 IC 리스트를 리턴한다.") {
                val result = routePlanner1.planRoute(startPoint, endPoint)

                result.first.shouldBeInstanceOf<List<Road>>()
                result.first.shouldBeInstanceOf<List<Junction>>()

                result.first.first().name shouldBe "강변대로"
                result.second.first().name shouldBe "양재IC"
            }
        }
    }

    describe("routePlanner2.planRoute 메소드는") {
        context("startPoint와 endPoint가 주어졌을 때") {
            it("부산의 도로 리스트와 IC 리스트를 리턴한다.") {
                val result = routePlanner2.planRoute(startPoint, endPoint)

                result.first.shouldBeInstanceOf<List<Road>>()
                result.first.shouldBeInstanceOf<List<Junction>>()

                result.first.first().name shouldBe "부산대로"
                result.second.first().name shouldBe "부산IC"
            }
        }
    }
})
