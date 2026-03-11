import com.internship.buffer.TerminalBuffer
import com.internship.types.Direction
import kotlin.test.Test
import kotlin.test.assertFailsWith


class CursorTest {
    @Test
    fun testScreenBounds() {
        val buffer = TerminalBuffer(initialSize = 20 to 10)
        buffer.moveCursor(Direction.DOWN, 100)
        buffer.moveCursor(Direction.RIGHT, amount = 100)
        val pos1 = buffer.getCursorPosition()
        assert(pos1.second == 9) {
            "The cursor has gone out of bounds on the rows axis, expected position 9, but got ${pos1.second}"
        }
        assert(pos1.first == 19) {
            "The cursor has gone out of bounds on the rows axis, expected position 19, but got ${pos1.first}"
        }
        buffer.moveCursor(Direction.UP, 100)
        buffer.moveCursor(Direction.LEFT, 100)
        val pos2 = buffer.getCursorPosition()
        assert(pos2.second == 0) {
            "The cursor has gone out of bounds on the rows axis, expected position 0, but got ${pos2.second}"
        }
        assert(pos2.first == 0) {
            "The cursor has gone out of bounds on the rows axis, expected position 0, but got ${pos2.first}"
        }
    }

    @Test
    fun testSetCursor() {
        val buffer = TerminalBuffer(initialSize = 20 to 10)
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
        assert(pos.first == 5 && pos.second == 6) { "The cursor position has not been set correctly, expected position <5, 6>, but got <${pos.first}, ${pos.second}>" }
    }
}