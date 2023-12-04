package com.example.study.ismycodethatweird.chapter4

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

@DisplayName("Code_4_25")
class Code_4_25Test : DescribeSpec({
    
    describe("HitPoint 객체는") {
        context("음수가 주어졌을 때") {
            
            val amount = -100
            
            it("IllegalArgumentException을 던진다.") {
                shouldThrow<IllegalArgumentException> { Code_4_25.HitPoint(amount) }
            }
        }
        
        context("damage 메소드는") {
            
            val hitPoint = Code_4_25.HitPoint(100)
            
            context("200이 주어졌을 때") {
                
                val damage = 200
                
                it("amount는 0이 된다.") {
                    hitPoint.damage(damage)
                    
                    hitPoint.amount shouldBe 0
                }
            }
        }
        
        context("isZero 메소드는") {

            val hitPoint = Code_4_25.HitPoint(100)
            
            context("데미지를 받아 amount가 0인 경우") {
                
                hitPoint.damage(200)
                
                it("true를 리턴한다.") {
                    hitPoint.isZero() shouldBe true
                }
            }
        }
    }
})
