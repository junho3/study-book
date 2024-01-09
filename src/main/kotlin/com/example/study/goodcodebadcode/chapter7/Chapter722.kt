package com.example.study.goodcodebadcode.chapter7

class Chapter722 {
    fun cantChangeColor(color: String): TextOption {
        val fonts = listOf(
            Font(name = "고딕체", color = "검정색"),
            Font(name = "굴림체", color = "검정색"),
        )

        val textOption = TextOption(
            fonts = fonts.map { it.copy() },
            fontSize = 1.1,
        )

        fonts.first().color = color

        return textOption
    }

    fun cantAddFont(name: String): TextOption {
        val fonts = mutableListOf(
            Font(name = "고딕체", color = "검정색"),
            Font(name = "굴림체", color = "검정색"),
        )

        val textOption = TextOption(
            fonts = fonts.map { it.copy() },
            fontSize = 1.1,
        )

        fonts.add(Font(name = name, color = "검정색"))

        return textOption
    }

    data class TextOption(
        val fonts: List<Font>,
        val fontSize: Double,
    )

    data class Font(
        val name: String,
        var color: String,
    )
}
