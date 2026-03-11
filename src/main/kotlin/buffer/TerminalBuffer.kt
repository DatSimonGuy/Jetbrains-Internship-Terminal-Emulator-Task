package com.internship.buffer

import com.internship.types.Direction
import com.internship.types.Screen
import com.internship.types.Scrollback
import com.internship.types.TerminalColor
import com.internship.types.TextStyle

class TerminalBuffer(
    initialSize: Pair<Int, Int> = 80 to 24,
    scrollBackSize: Int = 160
) {
    private var size = initialSize.copy()
    private var foreground: TerminalColor = TerminalColor.DEFAULT
    private var background: TerminalColor = TerminalColor.DEFAULT
    private val attributes: MutableMap<TextStyle, Boolean> = TextStyle.entries.associateWith { false }.toMutableMap()
    private val screen = Screen(initialSize)
    private val scrollback = Scrollback(scrollBackSize)
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
        val width = size.first
        val height = size.second
        val index = cursorPosition.second * width + cursorPosition.first
        val delta = direction.rMod * width * amount + direction.cMod * amount
        val newIndex = (index + delta).coerceIn(0, width * height - 1)
        val newRow = newIndex / width
        val newColumn = newIndex % width
        cursorPosition = newColumn to newRow
    }

    fun writeTextOnLine(char: Char) {
        screen.setCharacter(cursorPosition.first, cursorPosition.second, char)
        moveCursor(Direction.RIGHT, 1)
    }

    fun insertTextOnLine(char: Char) {
        screen.shiftRightAt(cursorPosition.first, cursorPosition.second)
        screen.setCharacter(cursorPosition.first, cursorPosition.second, char)
        moveCursor(Direction.RIGHT, 1)
    }

    fun fillLine(char: Char) {
        screen.setLine(cursorPosition.second, char)
    }

    fun addNewLine() {
        val movedLine = screen.addLine()
        scrollback.addLine(movedLine.joinToString("") { it.toString() })
    }

    fun clearScreen() {
        screen.clear()
    }

    fun clearScreenAndScrollback() {
        screen.clear()
        scrollback.clear()
    }

    override fun toString(): String {
        return screen.toString()
    }
}