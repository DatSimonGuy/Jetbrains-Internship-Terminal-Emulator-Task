package com.internship.types

enum class ColorType(
    val bitShift: Int
) {
    FOREGROUND(8),
    BACKGROUND(16)
}