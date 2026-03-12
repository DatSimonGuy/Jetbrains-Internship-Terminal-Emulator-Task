package com.internship.buffer

import com.internship.types.Direction
import com.internship.CharacterContainers.Screen
import com.internship.CharacterContainers.Scrollback
import com.internship.types.TerminalColor
import com.internship.types.TextStyle
import com.internship.types.maskToStyles
import com.internship.types.stylesToMask
import java.util.Locale.getDefault

class TerminalBuffer(
    screenSize: Pair<Int, Int> = 80 to 24,
    scrollBackSize: Int = 160
) {
    private var foreground: TerminalColor = TerminalColor.DEFAULT
    private var background: TerminalColor = TerminalColor.DEFAULT
    private val attributes: MutableMap<TextStyle, Boolean> = TextStyle.entries.associateWith { false }.toMutableMap()
    private val screen = Screen(screenSize)
    private val scrollback = Scrollback(scrollBackSize, screenSize.first)
    private var cursorPosition: Pair<Int, Int> = 0 to 0

    private val style get() = stylesToMask(attributes.filter { it.value }.map { it.key }, foreground, background)

    fun resize(newWidth: Int?, newHeight: Int?) {
        if (newWidth != null && newWidth < 2) {
            throw IllegalArgumentException("The terminal window needs to be at least 2 characters wide")
        }

        val (removedChars, removedStyles) = screen.resize(newWidth, newHeight)
        if (newWidth != null && cursorPosition.first > newWidth) {
            cursorPosition = (newWidth-1) to cursorPosition.second
        }
        if (newHeight != null && cursorPosition.second > newHeight) {
            cursorPosition = cursorPosition.first to (newHeight - 1)
        }
        for (i in 0..<removedChars.size) {
            scrollback.addLine(removedChars[i], removedStyles[i])
        }
        scrollback.resize(newWidth, null)
    }

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
        val size = screen.getSize()
        if (column >= size.first)
            throw IllegalArgumentException("The column is out of bounds for screen with size of ${size.first} by ${size.second}")
        if (row >= size.second)
            throw IllegalArgumentException("The row is out of bounds for screen with size of ${size.first} by ${size.second}")
        if (column < 0)
            throw IllegalArgumentException("The column needs to be greater than zero")
        if (row < 0)
            throw IllegalArgumentException("The row needs to be greater than zero")
        cursorPosition = column to row
        if (Character.isLowSurrogate(screen.getCharacter(column, row) ?: ' ')) {
            moveCursor(Direction.LEFT, 1)
        }
    }

    fun getCursorPosition(): Pair<Int, Int> {
        return cursorPosition
    }

    fun moveCursor(direction: Direction, amount: Int) {
        val size = screen.getSize()
        val width = size.first
        val height = size.second
        val index = cursorPosition.second * width + cursorPosition.first
        val delta = direction.rMod * width * amount + direction.cMod * amount
        var newIndex = index + delta

        while (newIndex >= width * height) {
            addNewLine()
            newIndex -= width
        }

        newIndex = newIndex.coerceAtLeast(0)

        val newRow = newIndex / width
        val newColumn = newIndex % width
        cursorPosition = newColumn to newRow

        if (Character.isLowSurrogate(screen.getCharacter(cursorPosition.first, cursorPosition.second) ?: ' ')) {
            moveCursor(if (direction == Direction.RIGHT) Direction.RIGHT else Direction.LEFT, 1)
        }
    }

    fun writeTextOnLine(character: String) {
        if (character.length > 1 && character.lowercase(getDefault()).contains(regex = Regex("[a-z]"))) {
            throw IllegalArgumentException("The input needs to be a single character or a special symbol/emoji")
        }
        if (character.length == 2 && cursorPosition.first == screen.getSize().first - 1) {
            moveCursor(Direction.RIGHT, 1)
        }
        for (char in character) {
            screen.setCell(
                cursorPosition.first,
                cursorPosition.second,
                char,
                style
            )
            moveCursor(Direction.RIGHT, 1)
        }
    }

    fun insertTextOnLine(character: String) {
        if (character.length > 1 && character.lowercase(getDefault()).contains(regex = Regex("[a-z]"))) {
            throw IllegalArgumentException("The input needs to be a single character or a special symbol/emoji")
        }
        if (character.length == 2 && cursorPosition.first == screen.getSize().first - 1) {
            moveCursor(Direction.LEFT, 1)
        }
        for (char in character) {
            screen.shiftRightAt(cursorPosition.first, cursorPosition.second)
            screen.setCell(
                cursorPosition.first,
                cursorPosition.second,
                char,
                style
            )
            moveCursor(Direction.RIGHT, 1)
        }
    }

    fun fillLine(char: Char) {
        screen.setLine(
            cursorPosition.second,
            char,
            style
        )
    }

    fun addNewLine() {
        val line = screen.addLine()
        scrollback.addLine(line.first, line.second)
    }

    fun getScreenLine(row: Int): String {
        return screen.getLine(row)
    }

    fun getScrollbackLine(row: Int): String {
        return scrollback.getLine(row)
    }

    fun clearScreen() {
        screen.clear()
    }

    fun getScreen(): String {
        return screen.toFormattedString()
    }

    fun getScreenAndScrollback(): String {
        return scrollback.toFormattedString() + screen.toFormattedString()
    }

    fun clearScreenAndScrollback() {
        screen.clear()
        scrollback.clear()
    }

    fun getScreenCharacterAtPosition(column: Int, row: Int): Char? {
        return screen.getCharacter(column, row)
    }

    fun getScreenAttributesAtPosition(column: Int, row: Int): Triple<List<TextStyle>, TerminalColor, TerminalColor> {
        return maskToStyles(screen.getAttributes(column, row) ?: 0)
    }

    fun getScrollbackCharacterAtPosition(column: Int, row: Int): Char? {
        return scrollback.getCharacter(column, row)
    }

    fun getScrollbackAttributesAtPosition(column: Int, row: Int): Triple<List<TextStyle>, TerminalColor, TerminalColor> {
        return maskToStyles(scrollback.getAttributes(column, row) ?: 0)
    }

    override fun toString(): String {
        return screen.toString()
    }
}