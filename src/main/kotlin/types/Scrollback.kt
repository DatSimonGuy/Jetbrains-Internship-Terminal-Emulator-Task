package com.internship.types

class Scrollback(
    initialSize: Int
) {
    val fields: MutableList<String?> = MutableList(initialSize) { null }

    fun addLine(line: String) {
        fields.addFirst(line)
        fields.removeLast()
    }

    fun clear() {
        fields.replaceAll { null }
    }
}