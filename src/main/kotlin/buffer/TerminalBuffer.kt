package com.internship.buffer

import com.internship.types.Direction
import com.internship.types.TerminalColor
import com.internship.types.TextStyle
import kotlin.math.max
import kotlin.math.min

class TerminalBuffer(
    initialSize: Pair<Int, Int> = 80 to 24,
    scrollBackSize: Int = 160
) {
    private var size = initialSize.copy()
    private var foreground: TerminalColor = TerminalColor.DEFAULT
    private var background: TerminalColor = TerminalColor.DEFAULT
    private val attributes: MutableMap<TextStyle, Boolean> = TextStyle.entries.associateWith { false }.toMutableMap()
    private val screen: List<MutableList<Char?>> = List(size.second) { MutableList(size.first) { null } }
    private val scrollBack: List<String?> = List(scrollBackSize) { null }
    private var cursorPosition: Pair<Int, Int> = 0 to 0

    fun setBackground(color: TerminalColor) {
        background = color
    }

    fun setForeground(color: TerminalColor) {
        foreground = color
    }

    fun setAttribute(attribute: TextStyle, value: Boolean) {
        attributes[attribute] = value
    }

    fun setCursorPosition(column: Int, row: Int) {
        if (column >= size.first)
            throw IllegalArgumentException("The column is out of bounds for screen with size of ${size.first} by ${size.second}")
        if (row >= size.second)
            throw IllegalArgumentException("The row is out of bounds for screen with size of ${size.first} by ${size.second}")
        if (column < 0)
            throw IllegalArgumentException("The column needs to be greater than zero")
        if (row < 0)
            throw IllegalArgumentException("The row needs to be greater than zero")
        cursorPosition = column to row
    }

    fun getCursorPosition(): Pair<Int, Int> {
        return cursorPosition
    }

    fun moveCursor(direction: Direction, amount: Int) {
        val newColumn = max(min(direction.cMod * amount + cursorPosition.first, size.first-1), 0)
        val newRow = max(min(direction.rMod * amount + cursorPosition.second, size.second-1), 0)
        cursorPosition = newColumn to newRow
    }
}