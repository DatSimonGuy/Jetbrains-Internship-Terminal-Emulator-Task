package com.internship.types

abstract class CharacterContainer (
    val initialHeight: Int,
    val initialWidth: Int
) {
    protected open val chars: MutableList<MutableList<Char>> = MutableList(initialHeight) { MutableList(initialWidth) { ' ' } }
    protected open val styles: MutableList<MutableList<Int>> = MutableList(initialHeight) { MutableList(initialWidth) { 0 } }

    protected var width = initialWidth
    protected var height = initialHeight

    abstract fun resize(newWidth: Int?, newHeight: Int?): Pair<List<MutableList<Char>>, List<MutableList<Int>>>

    fun fillArrays(newWidth: Int, newHeight: Int, flatChars: MutableList<Char>, flatStyles: MutableList<Int>) {
        val newCapacity = width * height

        while (flatChars.size < newCapacity) flatChars.add(' ')
        while (flatStyles.size < newCapacity) flatStyles.add(0)

        chars.clear()
        styles.clear()
        for (row in 0 until newHeight) {
            val rowStart = row * newWidth
            val rowEnd = rowStart + newWidth
            chars.add(flatChars.subList(rowStart, rowEnd).toMutableList())
            styles.add(flatStyles.subList(rowStart, rowEnd).toMutableList())
        }

        this.width = newWidth
        this.height = newHeight
    }

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
        chars.replaceAll( { MutableList(width) { ' ' } } )
        styles.replaceAll( { MutableList(width) { 0 } } )
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
        for(row in 0..<height) {
            for(col in 0..<width) {
                outputStr.append(getFormatted(col, row))
            }
            outputStr.append("\n")
        }
        return outputStr.toString()
    }
}