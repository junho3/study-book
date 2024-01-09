package com.example.study.goodcodebadcode.chapter6

class Chapter662Enum {
    enum class Animal {
        DOG, CAT, CAW, FISH
    }

    fun getAnimalSound(animal: Animal): String {
        return when (animal) {
            Animal.DOG -> "왈왈"
            Animal.CAT -> "애옹"
            Animal.CAW -> "음머"
            else -> error("울음소리가 없는 동물입니다.")
        }
    }
}
