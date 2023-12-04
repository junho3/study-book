package com.example.study.ismycodethatweird.chapter4

class Code_4_25 {
    
    class HitPoint(var amount: Int) {
        
        init {
            require(amount > MIN)
        }
        
        fun damage(damageAmount: Int) {
            val nextAmount = amount - damageAmount
            amount = MIN.coerceAtLeast(nextAmount) // MIN과 nextAmount 중에서 큰 값을 리턴
        }
        
        fun isZero(): Boolean {
            return amount == MIN
        }
        
        companion object {
            private const val MIN = 0
        }
    }
    
    class Member(
        val hitPoint: HitPoint,
        var state: String,
    ) {
        fun damage(damageAmount: Int) {
            hitPoint.damage(damageAmount)
            
            if (hitPoint.isZero()) {
                state = "DEAD"
            }
        }
    }
}
