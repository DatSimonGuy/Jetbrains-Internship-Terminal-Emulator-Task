import com.internship.buffer.TerminalBuffer
import com.internship.types.Direction
import kotlin.test.Test
import kotlin.test.assertContains


class EditingTest {
    /**
     * Tests basic writing behaviour
     *
     * Expected behaviour:
     * - The word exists in the buffer after being written
     * - The writing overrides existing characters
     */
    @Test
    fun writeTextTest() {
        val buffer = TerminalBuffer()
        buffer.writeTextOnLine("r")
        buffer.moveCursor(Direction.LEFT, 1)
        val textToWrite = "abecadło"
        textToWrite.forEach { c ->
            buffer.writeTextOnLine(c.toString())
        }
        // Test if the characters form the written word
        assertContains(buffer.toString(), "abecadło", message = "The buffer write failed")

        // Test if the word has overwritten the existing symbol
        assert(!buffer.toString().contains("r")) {
            "The write has not overwritten the existing character"
        }
    }

    /**
     * Tests insertion behaviour
     *
     * Expected behaviour:
     * - When writing backwards and moving one space left each time the resulting buffer should contain reversed string
     */
    @Test
    fun insertTextTest() {
        val buffer = TerminalBuffer()
        val textToWrite = "ołdaceba"
        textToWrite.forEach { c ->
            buffer.insertTextOnLine(c.toString())
            buffer.moveCursor(Direction.LEFT, 1)
        }
        // Test if the result contains the reversed string
        assertContains(buffer.toString(), "abecadło", message = "The buffer insertion failed")
    }

    /**
     * Tests line fill behaviour
     *
     * Expected behaviour
     * - when filling a line the buffer contains a string of characters as long as the buffer's width
     */
    @Test
    fun lineFillTest() {
        val width = 20
        val buffer = TerminalBuffer(width to 10)
        buffer.fillLine('a')
        // Test if the buffer contains a string of a length matching that of its width
        assertContains(buffer.toString(), "a".repeat(width), message = "The line has not been filled properly")
    }


    /**
     * Test the buffer's behaviour when adding a new line
     *
     * Expected behaviour:
     * - When adding a new line only one line is moved to the scrollback
     * - When adding a new line the topmost line is moved to the scrollback
     * - When the new line is added it is empty
     */
    @Test
    fun newLineTest() {
        val width = 20
        val buffer = TerminalBuffer(width to 10)
        buffer.fillLine('a')
        buffer.moveCursor(Direction.DOWN, 1)
        buffer.fillLine('b')
        buffer.setCursorPosition(19, 9)
        buffer.fillLine('c')
        buffer.addNewLine()
        // Test if the buffer still contains the b line after adding a new line
        assertContains(buffer.toString(), "b".repeat(width), message = "New line deleted two lines instead of one")

        // Test if the first line has been removed properly
        assert(!buffer.toString().contains("a".repeat(width))) {
            "The first line has not been deleted after adding a new one"
        }

        // Test if the added line is empty
        assertContains(buffer.toString().lines().last(), " ".repeat(width), message = "The new line is not empty")
    }

    /**
     * Tests the screen clearing behaviour
     *
     * Expected behaviour:
     * - The screen doesn't contain the content that was in the buffer previously
     */
    @Test
    fun clearScreenTest() {
        val height = 5
        val buffer = TerminalBuffer(20 to height)
        repeat(height) {
            buffer.fillLine('b')
            buffer.moveCursor(Direction.DOWN, 1)
        }
        buffer.clearScreen()
        // Test if the content has been removed after clearing the screen
        assert(!buffer.getScreen().contains('b')) {
            "The screen is not cleared fully"
        }
    }

    /**
     * Tests screen and scrollback clearing behaviour
     */
    @Test
    fun clearScreenAndScrollBackTest() {
        val height = 5
        val buffer = TerminalBuffer(20 to height, height)
        // fills both the screen and scrollback with b lines
        repeat(height) {
            buffer.addNewLine()
            buffer.fillLine('b')
        }
        buffer.clearScreenAndScrollback()
        // Test if the screen and scrollback are cleared
        assert(!buffer.getScreenAndScrollback().contains('b')) {
            "The screen is not cleared fully"
        }
    }
}