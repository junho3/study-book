package com.example.study.chapter7.code

class Chapter713StaticFactoryMethod {
    data class Student(
        val id: Int,
        val name: String,
        val age: Int,
        val score: Int?,
    ) {
        companion object {
            fun of(id: Int, name: String, age: Int, score: Int?): Student {
                return Student(id, name, age, score)
            }

            fun of(id: Int, name: String): Student {
                return Student(id, name, 8, null)
            }
        }
    }

    fun createStudent(id: Int, name: String, age: Int, score: Int?): Student {
        return Student.of(id, name, age, score)
    }

    fun createStudent(id: Int, name: String): Student {
        return Student.of(id, name)
    }
}
