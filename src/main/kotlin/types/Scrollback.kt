package com.internship.types

class Scrollback(
    initialSize: Int
) {
    val fields: MutableList<String?> = MutableList(initialSize) { null }

    fun addLine(line: String) {
        fields.addFirst(line)
        fields.removeLast()
    }

    fun getCharacter(column: Int, row: Int): Char? {
        return fields[row]?.get(row)
    }

    fun clear() {
        fields.replaceAll { null }
    }
}