import com.internship.buffer.TerminalBuffer
import com.internship.types.Direction
import kotlin.test.Test
import kotlin.test.assertContains

class BonusTasksTest {
    @Test
    fun resizeBufferTest() {
        val buffer = TerminalBuffer(20 to 5, 5)
        buffer.fillLine('a')
        buffer.resize(15, 2)

        assert(buffer.getScreenLine(0) == " ".repeat(15)) {
            "The screen's width has not been resized correctly, expected width 15, but got ${buffer.getScreenLine(0).length}"
        }

        assert(buffer.getScrollbackLine(4) == "a".repeat(15)) {
            "The scrollback's width has not been resized properly"
        }

        buffer.moveCursor(Direction.DOWN, 100)
        assert(buffer.getCursorPosition().second == 1) {
            "The screen's height has not been resized properly, expected height 2, but got ${buffer.getCursorPosition().second + 1}"
        }
    }

    @Test
    fun emojiWriteTest() {
        val buffer = TerminalBuffer()
        buffer.writeTextOnLine("👍")
        assert(buffer.getScreen().contains("👍")) {
            "The emoji has not been added successfully"
        }
        buffer.moveCursor(Direction.LEFT, 1)
        assert(buffer.getCursorPosition().first == 0) {
            "The cursor has moved to the middle of an emoji while moving left"
        }
        buffer.moveCursor(Direction.RIGHT, 1)
        assert(buffer.getCursorPosition().first == 2) {
            "The cursor has moved to the middle of an emoji while moving right"
        }
        buffer.moveCursor(Direction.DOWN, 1)
        buffer.moveCursor(Direction.LEFT, 1)
        buffer.moveCursor(Direction.UP, 1)
        assert(buffer.getCursorPosition().first == 0) {
            "The cursor has moved to the middle of an emoji while moving vertically"
        }
        buffer.setCursorPosition(1, 0)
        assert(buffer.getCursorPosition().first == 0) {
            "The cursor has moved to the middle of an emoji while setting the position manually"
        }
    }
}