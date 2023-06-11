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

    fun createStudent(id: Int, name: String, age: Int, score: Int?): Student {
        return Student(id, name, age, score)
    }

    fun createStudent(id: Int, name: String, age: Int): Student {
        return Student(id, name, age)
    }

    fun createStudent(id: Int, name: String): Student {
        return Student(id, name)
    }
}

private val logger = KotlinLogging.logger {}
