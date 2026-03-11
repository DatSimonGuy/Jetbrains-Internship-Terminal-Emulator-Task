package com.internship.types

enum class Direction (
    val cMod: Int,
    val rMod: Int,
) {
    UP(0, -1),
    DOWN(0, 1),
    RIGHT(1, 0),
    LEFT(-1, 0)
}