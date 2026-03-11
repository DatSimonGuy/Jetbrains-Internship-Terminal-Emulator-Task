package com.internship.types

fun stylesToMask(styles: List<TextStyle>, foreground: TerminalColor, background: TerminalColor): Int {
    var mask = styles.fold(0) { acc, style -> acc or style.mask }
    mask = mask or foreground.toMask(ColorType.FOREGROUND)
    mask = mask or background.toMask(ColorType.BACKGROUND)
    return mask
}

fun maskToAnsi(mask: Int): String {
    val styles = TextStyle.entries.filter { style ->
        mask and style.mask != 0
    }
    val fgCode = (mask shr ColorType.FOREGROUND.bitShift) and 0xFF
    val bgCode = (mask shr ColorType.BACKGROUND.bitShift) and 0xFF

    if (styles.isNotEmpty()) {
        val styleString = styles.joinToString(";") { it.toAnsiCode().toString() }
        return "\u001B[${styleString};3${fgCode};4${bgCode}m"
    } else {
        return "\u001B[${fgCode};${bgCode+10}m"
    }
}

fun maskToStyles(mask: Int): Triple<List<TextStyle>, TerminalColor, TerminalColor> {
    val styles = TextStyle.entries.filter { style ->
        mask and style.mask != 0
    }
    val fgCode = (mask shr ColorType.FOREGROUND.bitShift) and 0xFF
    val bgCode = (mask shr ColorType.BACKGROUND.bitShift) and 0xFF

    val foreground = TerminalColor.entries.firstOrNull {
        it.colorCode == fgCode
    } ?: TerminalColor.DEFAULT

    val background = TerminalColor.entries.firstOrNull {
        it.colorCode == bgCode
    } ?: TerminalColor.DEFAULT

    return Triple(styles, foreground, background)
}