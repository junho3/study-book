package com.example.study.ismycodethatweird.chapter6

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

@DisplayName("Code_6_26")
class Code_6_26Test : FunSpec({
    
    test("인터페이스를 상속 받은 클래스의 메소드 호출 테스트") {
        var shape: Code_6_26.Shape = Code_6_26.CircleWithInterface(10.0)
        println(shape.area())
        
        shape = Code_6_26.RectangleWithInterface(10.0, 10.0)
        println(shape.area())
    }
})
