package com.internship.types

enum class TextStyle(val mask: Int) {
    BOLD(1),
    ITALIC(1 shl 1),
    UNDERLINE(1 shl 2)
}

fun stylesToMask(styles: List<TextStyle>): Int = styles.fold(0) {
    acc, style -> acc or style.mask
}

fun maskToStyles(mask: Int): List<TextStyle> =
    TextStyle.entries.filter { style ->
        mask and style.mask != 0
    }