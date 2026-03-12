import com.internship.buffer.TerminalBuffer
import com.internship.types.Direction
import kotlin.test.Test
import kotlin.test.assertFailsWith


class CursorTest {
    /**
     * Tests cursor behaviour when going out of bounds
     *
     * Expected behaviour:
     * - The cursor wraps around the right side of the screen ending on a correct position in the new line
     * - The cursor creates new lines as necessary resulting in it being always on the last possible row
     * - The cursor doesn't reach negative row indexes when going left
     * - The cursor doesn't reach negative column indexes when going up
     * - The cursor moves to the correct position depending on the direction
     */
    @Test
    fun testScreenBounds() {
        val buffer = TerminalBuffer(screenSize = 20 to 10)
        // Test upper bounds
        buffer.moveCursor(Direction.DOWN, 100)
        buffer.moveCursor(Direction.RIGHT, amount = 101)
        val pos1 = buffer.getCursorPosition()
        // Test if the line is on the last possible row
        assert(pos1.second == 9) {
            "The cursor wrap has resulted in invalid position, expected 9, but got ${pos1.second}"
        }

        // Test if the line is on a correct position in the line
        assert(pos1.first == 1) {
            "The cursor wrap has resulted in invalid position, expected 1, but got ${pos1.first}"
        }

        // Test lower bounds
        buffer.moveCursor(Direction.UP, 100)
        buffer.moveCursor(Direction.LEFT, 100)
        val pos2 = buffer.getCursorPosition()
        // Test if the cursor can go past the first row
        assert(pos2.second == 0) {
            "The cursor has gone out of bounds on the rows axis, expected position 0, but got ${pos2.second}"
        }

        // Test if the cursor can go past the first column
        assert(pos2.first == 0) {
            "The cursor has gone out of bounds on the columns axis, expected position 0, but got ${pos2.first}"
        }

        // Test movement
        buffer.moveCursor(Direction.RIGHT, 45)
        val pos3 = buffer.getCursorPosition()
        // Test if the cursor moves and wraps to the next line when going right
        assert(pos3.first == 5 && pos3.second == 2) {
            "The cursor has not been moved correctly, expected position <5, 2>, but got <${pos3.first}, ${pos3.second}>"
        }

        buffer.moveCursor(Direction.LEFT, 22)
        val pos4 = buffer.getCursorPosition()
        // Test if the cursor moved up when reaching the start of a given row and ended up in the expected spot
        assert(pos4.first == 3 && pos4.second == 1) {
            "The cursor has not been moved correctly, expected position <3, 1>, but got <${pos4.first}, ${pos4.second}>"
        }

        buffer.moveCursor(Direction.DOWN, 3)
        val pos5 = buffer.getCursorPosition()
        // Test if the cursor moved down the specified amount of spaces
        assert(pos5.first == 3 && pos5.second == 4) {
            "The cursor has not been moved correctly, expected position <3, 4>, but got <${pos5.first}, ${pos5.second}>"
        }

        buffer.moveCursor(Direction.UP, 1)
        val pos6 = buffer.getCursorPosition()
        // Test if the cursor moved up the specified amount of spaces
        assert(pos6.first == 3 && pos6.second == 3) {
            "The cursor has not been moved correctly, expected position <3, 3>, but got <${pos6.first}, ${pos6.second}>"
        }
    }

    /**
     * Tests cursor behaviour when setting its position directly
     * Expected behaviour:
     * - The method throws IllegalArgumentException whenever the position is out of bounds
     * - The method sets the position to the specified one if the coordinates are valid
     */
    @Test
    fun testSetCursor() {
        val buffer = TerminalBuffer(screenSize = 20 to 10)
        // Test input verification
        assertFailsWith<IllegalArgumentException>(message = "The cursor's column can be set out of bounds") {
            buffer.setCursorPosition(20, 5)
        }
        assertFailsWith<IllegalArgumentException>(message = "The cursor's row can be set out of bounds") {
            buffer.setCursorPosition(5, 10)
        }
        assertFailsWith<IllegalArgumentException>(message = "The cursor's column can be set to negative values") {
            buffer.setCursorPosition(-1, 5)
        }
        assertFailsWith<IllegalArgumentException>(message = "The cursor's row can be set to negative values") {
            buffer.setCursorPosition(5, -1)
        }

        buffer.setCursorPosition(5, 6)
        val pos = buffer.getCursorPosition()
        // Test if the position is set accordingly
        assert(pos.first == 5 && pos.second == 6) {
            "The cursor position has not been set correctly, expected position <5, 6>, but got <${pos.first}, ${pos.second}>"
        }
    }
}