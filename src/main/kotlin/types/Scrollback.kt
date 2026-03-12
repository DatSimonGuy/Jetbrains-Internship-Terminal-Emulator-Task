package com.internship.types

class Scrollback(
    initialSize: Int,
    initialWidth: Int
): CharacterContainer(initialSize, initialWidth) {
    fun addLine(line: List<Char>, styleLine: List<Int>) {
        chars.addFirst(line.toMutableList())
        styles.addFirst(styleLine.toMutableList())
        chars.removeLast()
        styles.removeLast()
    }

    override fun toFormattedString(): String {
        val outputStr = StringBuilder()
        for(row in 1..height) {
            for(col in 1..width) {
                outputStr.append(getFormatted(width - col, height - row))
            }
            outputStr.append("\n")
        }
        return outputStr.toString()
    }

    override fun resize(newWidth: Int?, newHeight: Int?): Pair<List<MutableList<Char>>, List<MutableList<Int>>> {
        val flatChars = chars.flatten().toMutableList()
        val flatStyles = styles.flatten().toMutableList()

        val newCapacity = (newWidth ?: width) * (newHeight ?: height)

        if (flatChars.size > newCapacity) {
            flatChars.subList(newCapacity, flatChars.size).clear()
            flatStyles.subList(newCapacity, flatStyles.size).clear()
        }

        super.fillArrays(newWidth ?: width, newHeight ?: height, flatChars, flatStyles)

        return Pair(emptyList(), emptyList())
    }
}