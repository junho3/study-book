package com.example.study.goodcodebadcode.chapter8

class KotlinDelegation {
    interface Base {
        fun print(): Int
    }

    class BaseImpl(val x: Int) : Base {
        override fun print(): Int {
            print(x)
            return x
        }
    }

    class Derived(b: Base) : Base by b

    fun main(x: Int): Int {
        val b = BaseImpl(x)

        return Derived(b).print()
    }
}
