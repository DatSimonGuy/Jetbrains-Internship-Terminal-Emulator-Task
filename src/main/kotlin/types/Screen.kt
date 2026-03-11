package com.internship.types

class Screen (
    initialSize: Pair<Int, Int>
) {
    private var width = initialSize.first
    private var height = initialSize.second

    private val chars: MutableList<MutableList<Char>> = MutableList(height) { MutableList(width) { ' ' } }
    private val styles: MutableList<MutableList<Int>> = MutableList(height) { MutableList(width) { 0 } }

    fun getCharacter(column: Int, row: Int): Char? {
        return try {
            chars[row][column]
        } catch (_: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    fun getAttributes (column: Int, row: Int): Int? {
        return try {
            styles[row][column]
        } catch (_: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    fun setCell(column: Int, row: Int, char: Char, style: Int) {
        chars[row][column] = char
        styles[row][column] = style
    }

    fun setLine(row: Int, char: Char, style: Int) {
        chars[row].replaceAll { char }
        styles[row].replaceAll { style }
    }

    fun addLine(): Pair<MutableList<Char>, MutableList<Int>> {
        val firstRow = chars[0]
        val firstStyles = styles[0]
        chars.removeFirst()
        styles.removeFirst()
        chars.addLast(MutableList(width) { ' ' })
        styles.addLast(MutableList(width) { 0 })
        return firstRow to firstStyles
    }

    fun clear() {
        chars.forEach {
            it.replaceAll { ' ' }
        }
        styles.forEach {
            it.replaceAll { 0 }
        }
    }

    fun shiftRightAt(column: Int, row: Int) {
        var index = (height-1) * width + width-1
        while ((index % width) > column || (index / width) > row) {
            val prevChar = getCharacter((index-1)%width, (index-1)/width)
            val prevAttr = getAttributes((index-1)%width, (index-1)/width)
            setCell(index % width, index / width, prevChar!!, prevAttr!!)
            index -= 1
        }
    }

    override fun toString(): String {
        return chars.joinToString("\n") { row ->
            row.joinToString("") { it.toString() }
        }
    }
}