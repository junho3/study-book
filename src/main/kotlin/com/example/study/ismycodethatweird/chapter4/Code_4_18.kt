package com.example.study.ismycodethatweird.chapter4

class Code_4_18 {
    
    class AttackPower(val value: Int) {
        
        init {
            require(value > MIN)
        }
        
        fun reinforce(increment: AttackPower): AttackPower {
            return AttackPower(value + increment.value)
        }
        
        fun disable(): AttackPower {
            return AttackPower(MIN)
        }
        
        companion object {
            const val MIN = 0
        }
    }
    
    class Weapon(val attackPower: AttackPower) {
        fun reinforce(increment: AttackPower): Weapon {
            val reinforced = attackPower.reinforce(increment)
            return Weapon(reinforced)
        }
    }
}
