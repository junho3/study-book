package com.example.study.chapter7.code

import mu.KotlinLogging

class Chapter713Constructor {
    data class Student(
        val id: Int,
        val name: String,
        val age: Int,
        val score: Int? = null,
    ) {
        init {
            logger.info { "주생성자 호출" }
        }

        constructor(id: Int, name: String) : this(id, name, 8, null) {
            logger.info { "부생성자 호출" }
        }
    }

    fun createStudentByMainConstructor(): Student {
        return Student(1, "홍길동", 13, 100)
    }

    fun createStudentByMainConstructorWithDefaultScore(): Student {
        return Student(2, "김길동", 10)
    }

    fun createStudentBySubConstructor(): Student {
        return Student(3, "이길동")
    }
}

private val logger = KotlinLogging.logger {}
