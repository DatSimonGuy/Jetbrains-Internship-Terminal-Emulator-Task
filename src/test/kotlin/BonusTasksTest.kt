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
}