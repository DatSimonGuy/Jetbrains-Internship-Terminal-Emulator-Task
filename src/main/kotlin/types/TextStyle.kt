package com.internship.types

enum class TextStyle(val mask: Int) {
    BOLD(1),
    ITALIC(1 shl 1),
    UNDERLINE(1 shl 2);

    fun toAnsiCode(): Char {
        return when(this) {
            BOLD -> '1'
            ITALIC -> '3'
            UNDERLINE -> '4'
        }
    }
}
