package com.internship.types

class Scrollback(
    initialSize: Int,
    val initialWidth: Int
) {
    val chars: MutableList<List<Char>> = MutableList(initialSize) { List(initialWidth) { ' ' } }
    val styles: MutableList<List<Int>> = MutableList(initialSize) { List(initialWidth) { 0 } }

    fun addLine(line: List<Char>, styleLine: List<Int>) {
        chars.addFirst(line)
        styles.addFirst(styleLine)
        chars.removeLast()
        styles.removeLast()
    }

    fun getCharacter(column: Int, row: Int): Char? {
        return try {
            chars[row][column]
        } catch (_: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    fun getAttribute(column: Int, row: Int): Int? {
        return try {
            styles[row][column]
        } catch (_: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    fun clear() {
        chars.replaceAll( { List(initialWidth) { ' ' } } )
        styles.replaceAll( { List(initialWidth) { 0 } } )
    }
}