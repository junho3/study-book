package com.example.study.ismycodethatweird.chapter4

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

@DisplayName("Code_4_18")
class Code_4_18Test : DescribeSpec({
    
    describe("reinforce 메소드는") {
        
        val attackPower = Code_4_18.AttackPower(20)
        
        context("AttackPower 15가 주어졌을 때") {
            
            val reinforceAttackPower = Code_4_18.AttackPower(15)
            
            it("AttackPower는 35가 되고, 변경 전과 변경 후 서로 영향을 주지 않는다.") {
                val actual = attackPower.reinforce(reinforceAttackPower)

                actual.value shouldBe 35
                attackPower.value shouldBe 20
            }
        }
    }
})
