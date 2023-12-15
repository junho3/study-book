package com.example.study.ismycodethatweird.chapter7

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs

@DisplayName("Code_7_14")
class Code_7_14Test : DescribeSpec({
    
    describe("Party 클래스는") {
        context("members를 받아서") {
            
            val party = Code_7_14.Party()
            val newMember = "애플"
            
            it("새로운 Party 객체를 생성한다.") {
                val newParty = party.add(newMember)
                
                party shouldNotBeSameInstanceAs newParty
            }
        }
        
        context("중복된 member가 주어질 경우") {
            
            val party = Code_7_14.Party(listOf("애플"))
            val newMember = "애플"
            
            it("IllegalArgumentException을 던진다.") {
                shouldThrow<IllegalArgumentException> { 
                    party.add(newMember)
                }
            }
        }
        
        context("파티가 최대 인원인 경우") {

            val party = Code_7_14.Party(listOf("애플", "바나나", "멜론", "딸기"))
            val newMember = "수박"

            it("IllegalArgumentException을 던진다.") {
                shouldThrow<IllegalArgumentException> {
                    party.add(newMember)
                }
            }
        }
    }
})
