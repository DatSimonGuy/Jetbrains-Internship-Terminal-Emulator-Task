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
        for(row in 1..initialHeight) {
            for(col in 1..initialWidth) {
                outputStr.append(getFormatted(initialWidth - col, initialHeight - row))
            }
            outputStr.append("\n")
        }
        return outputStr.toString()
    }
}