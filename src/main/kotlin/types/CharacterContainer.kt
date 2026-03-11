package com.internship.types

open class CharacterContainer (
    initialHeight: Int,
    val initialWidth: Int
) {
    protected open val chars: MutableList<MutableList<Char>> = MutableList(initialHeight) { MutableList(initialWidth) { ' ' } }
    protected open val styles: MutableList<MutableList<Int>> = MutableList(initialHeight) { MutableList(initialWidth) { 0 } }

    fun getLine(row: Int): String {
        return chars[row].joinToString("") { it.toString() }
    }

    fun getCharacter(column: Int, row: Int): Char? {
        return try {
            chars[row][column]
        } catch (_: ArrayIndexOutOfBoundsException) {
            null
        }
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
}