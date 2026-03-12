package com.internship.types

class Screen (
    initialSize: Pair<Int, Int>
): CharacterContainer(initialSize.second, initialSize.first) {

    override fun resize(newWidth: Int?, newHeight: Int?): Pair<List<MutableList<Char>>, List<MutableList<Int>>> {
        val flatChars = chars.flatten().toMutableList()
        val flatStyles = styles.flatten().toMutableList()
        val removedLines = mutableListOf<MutableList<Char>>()
        val removedStyles = mutableListOf<MutableList<Int>>()

        val newCapacity = (newWidth ?: width) * (newHeight ?: height)

        while (flatChars.size > newCapacity) {
            removedLines.add(flatChars.subList(0, width).toMutableList())
            removedStyles.add(flatStyles.subList(0, width).toMutableList())
            flatChars.subList(0, width).clear()
            flatStyles.subList(0, width).clear()
            addLine()
        }

        super.fillArrays(newWidth ?: width, newHeight ?: height, flatChars, flatStyles)

        return Pair(removedLines, removedStyles)
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

    fun shiftRightAt(column: Int, row: Int) {
        var index = (height-1) * width + width-1
        while ((index % width) > column || (index / width) > row) {
            val prevChar = getCharacter((index-1)%width, (index-1)/width)
            val prevAttr = getAttributes((index-1)%width, (index-1)/width)
            setCell(index % width, index / width, prevChar!!, prevAttr!!)
            index -= 1
        }
    }
}