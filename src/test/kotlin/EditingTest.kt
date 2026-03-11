import com.internship.buffer.TerminalBuffer
import com.internship.types.Direction
import kotlin.test.Test
import kotlin.test.assertContains


class EditingTest {
    @Test
    fun testWrite() {
        val buffer = TerminalBuffer()
        buffer.writeTextOnLine('r')
        buffer.moveCursor(Direction.LEFT, 1)
        val textToWrite = "abecadło"
        textToWrite.forEach { c ->
            buffer.writeTextOnLine(c)
        }
        assertContains(buffer.toString(), "abecadło", message = "The buffer write failed")
        assert(!buffer.toString().contains("r")) {
            "The write has not overwritten the existing character"
        }
    }

    @Test
    fun testInsert() {
        val buffer = TerminalBuffer()
        val textToWrite = "ołdaceba"
        textToWrite.forEach { c ->
            buffer.insertTextOnLine(c)
            buffer.moveCursor(Direction.LEFT, 1)
        }
        assertContains(buffer.toString(), "abecadło", message = "The buffer insertion failed")
    }

    @Test
    fun testFill() {
        val width = 20
        val buffer = TerminalBuffer(width to 10)
        buffer.fillLine('a')
        assertContains(buffer.toString(), "a".repeat(width), message = "The line has not been filled properly")
    }

    @Test
    fun testNewLine() {
        val width = 20
        val buffer = TerminalBuffer(width to 10)
        buffer.fillLine('a')
        buffer.moveCursor(Direction.DOWN, 1)
        buffer.fillLine('b')
        buffer.setCursorPosition(19, 9)
        buffer.fillLine('c')
        buffer.addNewLine()
        assertContains(buffer.toString(), "b".repeat(width), message = "New line deleted two lines instead of one")
        assert(!buffer.toString().contains("a".repeat(width))) {
            "The first line has not been deleted after adding a new one"
        }
        assertContains(buffer.toString().lines().last(), " ".repeat(width), message = "The new line is not empty")
    }
}