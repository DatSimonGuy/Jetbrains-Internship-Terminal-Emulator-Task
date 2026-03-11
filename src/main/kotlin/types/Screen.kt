package com.internship.types

import javax.swing.tree.ExpandVetoException

class Screen (
    val initialSize: Pair<Int, Int>
) {
    private var width = initialSize.first
    private var height = initialSize.second

    private val fields: List<MutableList<Char?>> = List(height) { MutableList(width) { null } }

    fun getCharacter(column: Int, row: Int): Char? {
        try {
            return fields[row][column]
        } catch (e: Exception) {
            return null
        }

    }

    fun setCharacter(column: Int, row: Int, value: Char) {
        fields[row][column] = value
    }

    fun shiftRightAt(column: Int, row: Int) {
        var index = (height-1) * width + width-1
        while ((index % width) > column || (index / width) > row) {
            val previousValue = getCharacter((index-1)%width, (index-1)/width)
            if (previousValue != null)
                setCharacter(index % width, index / width, previousValue)
            index -= 1
        }
    }

    override fun toString(): String {
        return fields.joinToString("\n") { row ->
            row.joinToString("") { it?.toString() ?: "" }
        }
    }
}