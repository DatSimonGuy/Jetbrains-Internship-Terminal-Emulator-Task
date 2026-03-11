import com.internship.buffer.TerminalBuffer
import com.internship.types.Direction
import kotlin.test.Test
import kotlin.test.assertContains


class EditingTest {
    @Test
    fun TestWrite() {
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
    fun TestInsert() {
        val buffer = TerminalBuffer()
        val textToWrite = "ołdaceba"
        textToWrite.forEach { c ->
            buffer.insertTextOnLine(c)
            buffer.moveCursor(Direction.LEFT, 1)
        }
        assertContains(buffer.toString(), "abecadło", message = "The buffer insertion failed")
    }

    @Test
    fun TestFill() {
        val width = 20
        val buffer = TerminalBuffer(width to 10)
        buffer.fillLine('a')
        assertContains(buffer.toString(), "a".repeat(width), message = "The line has not been filled properly")
    }
}