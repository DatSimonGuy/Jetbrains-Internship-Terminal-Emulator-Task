package com.internship.buffer

import com.internship.types.TerminalColor
import com.internship.types.TextStyle

class TerminalBuffer(
    val initialSize: Pair<Int, Int> = 80 to 24,
    val scrollBackSize: Int = 160
) {
    private var foreground: TerminalColor = TerminalColor.DEFAULT
    private var background: TerminalColor = TerminalColor.DEFAULT
    private val attributes: MutableMap<TextStyle, Boolean> = TextStyle.entries.associateWith { false }.toMutableMap()

    fun setBackground(color: TerminalColor) {
        background = color
    }

    fun setForeground(color: TerminalColor) {
        foreground = color
    }

    fun setAttribute(attribute: TextStyle, value: Boolean) {
        attributes[attribute] = value
    }
}