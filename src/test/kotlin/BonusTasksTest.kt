import com.internship.buffer.TerminalBuffer
import com.internship.types.Direction
import kotlin.test.Test

class BonusTasksTest {
    /**
     * Tests buffer resize behaviour.
     *
     * Expected behaviour:
     * - Screen width must match the new width
     * - Scrollback width resizes along with the screen
     * - The height and cursor boundaries must match the new height
     */
    @Test
    fun resizeBufferTest() {
        val buffer = TerminalBuffer(20 to 5, 5)
        buffer.fillLine('a')
        buffer.resize(15, 2)

        // Check if the first line has been properly pushed to the scrollback
        assert(buffer.getScreenLine(0) == " ".repeat(15)) {
            "The screen's width has not been resized correctly, expected width 15, but got ${buffer.getScreenLine(0).length}"
        }
        // Check if the scrollback has properly received the line and if the line is of correct width
        assert(buffer.getScrollbackLine(4) == "a".repeat(15)) {
            "The scrollback's width has not been resized properly"
        }
        // Check if the cursor has not gone out of bounds and based on its position calculate the screen's height
        buffer.moveCursor(Direction.DOWN, 100)
        assert(buffer.getCursorPosition().second == 1) {
            "The screen's height has not been resized properly, expected height 2, but got ${buffer.getCursorPosition().second + 1}"
        }
    }

    /**
     * Tests terminal buffer's handling of multi character symbols
     *
     * Expected behaviour:
     * - adding an emoji correctly places and displays it on the screen
     * - cursor movement cannot end in the middle of a special character
     */
    @Test
    fun emojiWriteTest() {
        val buffer = TerminalBuffer()
        buffer.writeTextOnLine("👍")
        // Check if the screen contains the character
        assert(buffer.getScreen().contains("👍")) {
            "The emoji has not been added successfully"
        }
        // Check if the cursor has moved to the centre of the emoji when moving left
        buffer.moveCursor(Direction.LEFT, 1)
        assert(buffer.getCursorPosition().first == 0) {
            "The cursor has moved to the middle of an emoji while moving left"
        }
        // Check if the cursor has moved to the centre of the emoji when moving right
        buffer.moveCursor(Direction.RIGHT, 1)
        assert(buffer.getCursorPosition().first == 2) {
            "The cursor has moved to the middle of an emoji while moving right"
        }
        // Check if the cursor has moved to the centre of the emoji when moving up from directly below
        buffer.moveCursor(Direction.DOWN, 1)
        buffer.moveCursor(Direction.LEFT, 1)
        buffer.moveCursor(Direction.UP, 1)
        assert(buffer.getCursorPosition().first == 0) {
            "The cursor has moved to the middle of an emoji while moving vertically"
        }
        // Check if the cursor has moved to the centre of the emoji when setting its position
        buffer.setCursorPosition(1, 0)
        assert(buffer.getCursorPosition().first == 0) {
            "The cursor has moved to the middle of an emoji while setting the position manually"
        }
    }
}