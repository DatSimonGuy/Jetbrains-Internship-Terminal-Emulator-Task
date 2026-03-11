import com.internship.buffer.TerminalBuffer
import com.internship.types.Direction
import kotlin.test.Test
import kotlin.test.assertContains


class EditingTest {
    @Test
    fun TestWrite() {
        val buffer = TerminalBuffer()
        val textToWrite = "abecadło"
        textToWrite.forEach { c ->
            buffer.writeTextOnLine(c)
        }
        assertContains(buffer.toString(), "abecadło", message = "The buffer write failed")
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
}