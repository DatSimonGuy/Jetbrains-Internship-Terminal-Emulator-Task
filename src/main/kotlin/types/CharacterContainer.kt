package com.internship.types

open class CharacterContainer (
    val initialHeight: Int,
    val initialWidth: Int
) {
    protected open val chars: MutableList<MutableList<Char>> = MutableList(initialHeight) { MutableList(initialWidth) { ' ' } }
    protected open val styles: MutableList<MutableList<Int>> = MutableList(initialHeight) { MutableList(initialWidth) { 0 } }

    fun getLine(row: Int): String {
        return chars[row].joinToString("") { it.toString() }
    }

    fun getCharacter(column: Int, row: Int): Char? {
        return chars.getOrNull(row)?.getOrNull(column)
    }

    fun getFormatted(column: Int, row: Int): String? {
        val attrText = maskToAnsi(getAttributes(column, row) ?: return null)
        val character = chars.getOrNull(row)?.getOrNull(column) ?: return null
        return "$attrText$character\u001B[0m"
    }

    fun getAttributes(column: Int, row: Int): Int? {
        return try {
            styles[row][column]
        } catch (_: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    fun clear() {
        chars.replaceAll( { MutableList(initialWidth) { ' ' } } )
        styles.replaceAll( { MutableList(initialWidth) { 0 } } )
    }

    override fun toString(): String {
        return chars.joinToString("\n") { row ->
            row.joinToString("") {
                it.toString()
            }
        }
    }

    open fun toFormattedString(): String {
        val outputStr = StringBuilder()
        for(row in 0..<initialHeight) {
            for(col in 0..<initialWidth) {
                outputStr.append(getFormatted(col, row))
            }
            outputStr.append("\n")
        }
        return outputStr.toString()
    }
}