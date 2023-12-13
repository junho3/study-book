package com.example.study.ismycodethatweird.chapter6

class Code_6_26 {
    
    data class Rectangle(
        val width: Double,
        val height: Double,
    ) {
        fun area(): Double {
            return width * height
        }
    }
    
    data class Circle(
        val radius: Double,
    ) {
        fun area(): Double {
            return radius * radius * Math.PI
        }
    }
    
    interface Shape {
        fun area(): Double
    }
    
    data class RectangleWithInterface(
        val width: Double,
        val height: Double,
    ): Shape {
        override fun area(): Double {
            return width * height
        }
    }
    
    data class CircleWithInterface(
        val radius: Double,
    ): Shape {
        override fun area(): Double {
            return radius * radius * Math.PI
        }
    }
}
