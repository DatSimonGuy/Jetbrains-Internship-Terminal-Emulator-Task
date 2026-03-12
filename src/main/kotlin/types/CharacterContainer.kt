package com.internship.types

abstract class CharacterContainer (
    initialHeight: Int,
    initialWidth: Int
) {
    protected open val chars: MutableList<MutableList<Char>> = MutableList(initialHeight) { MutableList(initialWidth) { ' ' } }
    protected open val styles: MutableList<MutableList<Int>> = MutableList(initialHeight) { MutableList(initialWidth) { 0 } }

    protected var width = initialWidth
    protected var height = initialHeight

    abstract fun resize(newWidth: Int?, newHeight: Int?): Pair<List<MutableList<Char>>, List<MutableList<Int>>>

    fun getSize(): Pair<Int, Int> {
        return width to height
    }

    fun fillArrays(newWidth: Int, newHeight: Int, flatChars: MutableList<Char>, flatStyles: MutableList<Int>) {
        val newCapacity = newWidth * newHeight

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

    open fun toFormattedString(isReverse: Boolean = false): String {
        val output = StringBuilder()

        val range = if(isReverse) (0 ..< height).reversed() else (0 ..< height)
        for (row in range) {
            var col = if(isReverse) width-1 else 0
            while (if (isReverse) col >= 0 else col < width) {
                val char = getCharacter(col, row) ?: ' '
                val attr = getAttributes(col, row) ?: 0

                if (Character.isHighSurrogate(char) && col + 1 < width) {
                    val next = getCharacter(col + 1, row)
                    if (next != null && Character.isLowSurrogate(next)) {
                        val combined = String(charArrayOf(char, next))
                        output.append(combined)
                        col += 2
                        continue
                    }
                }

                output.append(maskToAnsi(attr))
                output.append(char)
                output.append("\u001B[0m")
                col += if (isReverse) -1 else 1
            }
            if (row < height - 1) output.append("\n")
        }

        return output.toString()
    }
}