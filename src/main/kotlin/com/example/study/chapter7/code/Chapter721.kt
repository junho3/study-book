package com.example.study.chapter7.code

class Chapter721 {
    fun changeColor(color: String): TextOption {
        val fonts = listOf(
            Font(name = "고딕체", color = "검정색"),
            Font(name = "굴림체", color = "검정색"),
        )

        val textOption = TextOption(
            fonts = fonts,
            fontSize = 1.1,
        )

        fonts.first().color = color

        return textOption
    }

    fun addFont(name: String): TextOption {
        val fonts = mutableListOf(
            Font(name = "고딕체", color = "검정색"),
            Font(name = "굴림체", color = "검정색"),
        )

        val textOption = TextOption(
            fonts = fonts,
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
