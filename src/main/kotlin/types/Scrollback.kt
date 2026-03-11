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
}