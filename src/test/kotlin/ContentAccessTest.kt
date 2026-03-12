import com.internship.buffer.TerminalBuffer
import com.internship.types.Direction
import com.internship.types.TerminalColor
import com.internship.types.TextStyle
import kotlin.test.Test
import kotlin.test.assertContains


class ContentAccessTest {
    /**
     * Tests if the character getters work properly
     *
     * Expected behaviour:
     * - a correct character is received from its position on the screen
     * - a correct character is received from its position in scrollback
     */
    @Test
    fun getCharacterAtPositionTest() {
        val buffer = TerminalBuffer(80 to 25)
        buffer.setCursorPosition(20, 20)
        buffer.writeTextOnLine("a")
        val char1 = buffer.getScreenCharacterAtPosition(20, 20)
        // Check if the written character can be received from the specified position on screen
        assert(char1 == 'a') {
            "Wrong character has been returned from screen, expected 'a', but got '${char1}'"
        }

        buffer.setCursorPosition(5, 0)
        buffer.writeTextOnLine("b")
        buffer.addNewLine()
        // Check if the written character can be received from the specified position in scrollback
        val char2 = buffer.getScrollbackCharacterAtPosition(5, 0)
        assert(char2 == 'b') {
            "Wrong character has been returned from scrollback, expected 'b', but got '${char2}'"
        }
    }

    /**
     * Tests if the attribute getters work properly
     *
     * Expected behaviour:
     * - the character written on screen has all the set attributes
     * - the character written in scrollback has all the set attributes
     */
    @Test
    fun getAttributeAtPositionTest() {
        val buffer = TerminalBuffer(80 to 25)
        buffer.setAttribute(TextStyle.BOLD, true)
        buffer.setForeground(TerminalColor.WHITE)
        buffer.setBackground(TerminalColor.BLUE)
        buffer.setCursorPosition(20, 20)
        buffer.writeTextOnLine("x")
        val attrs1 = buffer.getScreenAttributesAtPosition(20, 20)
        // Check if the attributes match with the specified ones
        assert(attrs1.first.contains(TextStyle.BOLD) && attrs1.second == TerminalColor.WHITE && attrs1.third == TerminalColor.BLUE) {
            "Style changes have not been applied on screen during character insertion"
        }

        buffer.setAttribute(TextStyle.UNDERLINE, true)
        buffer.setForeground(TerminalColor.YELLOW)
        buffer.setBackground(TerminalColor.GREEN)
        buffer.setCursorPosition(5, 0)
        buffer.writeTextOnLine("x")
        buffer.addNewLine()
        val attrs2 = buffer.getScrollbackAttributesAtPosition(5, 0)
        // Check if the attributes match with the specified ones
        assert(attrs2.first.contains(TextStyle.UNDERLINE) && attrs2.second == TerminalColor.YELLOW && attrs2.third == TerminalColor.GREEN) {
            "Style changes have not been applied on scrollback during character insertion"
        }
    }

    /**
     * Tests joining of the line to a string
     *
     * Expected behaviour:
     * - filling a line with a character results in a string of the same size with this character
     * - moving the line to a scrollback doesn't modify it and the line is converted correctly
     */
    @Test
    fun getLineAsStringTest() {
        val width = 5
        val buffer = TerminalBuffer(width to 10)
        buffer.moveCursor(Direction.DOWN, 2)
        buffer.fillLine('s')
        val line1 = buffer.getScreenLine(2)
        // Test if the line contains all the characters on screen
        assert(line1 == "s".repeat(width)) {
            "Screen line getter returned invalid value, expected sssss, but got '${line1}'"
        }
        repeat(4) {
            buffer.addNewLine()
        }
        val line2 = buffer.getScrollbackLine(1)
        // Test if the line contains all the characters in scrollback
        assert(line2 == "s".repeat(width)) {
            "Scrollback line getter returned invalid value, expected sssss, but got '${line2}'"
        }
    }

    /**
     * Tests if the screen getter returns complete text with its attributes
     *
     * Expected behaviour:
     * - both corners of the screen are present in the final string and contain the ANSI code
     */
    @Test
    fun getEntireScreenTest() {
        val buffer = TerminalBuffer(6 to 6)
        buffer.setForeground(TerminalColor.RED)
        buffer.moveCursor(Direction.DOWN, 1)
        buffer.insertTextOnLine("t")
        buffer.setCursorPosition(5, 5)
        buffer.insertTextOnLine("6")
        val screen = buffer.getScreen()
        // Check top left and bottom right corner for the presence of their respective characters
        assertContains(screen, ("\u001B[31;49m6\u001B[0m"), message = "The screen display is incomplete")
        assertContains(screen, ("\u001B[31;49mt\u001B[0m"), message = "The screen display is incomplete")
    }

    /**
     * Tests if the screen and scrollback getter returns complete text with its attributes
     *
     * Expected behaviour:
     * - The character on the screen is present in the returned string and contains ANSI code
     * - The character in scrollback is present in the returned string and contains ANSI code
     */
    @Test
    fun getEntireScreenAndScrollbackTest() {
        val buffer = TerminalBuffer(6 to 6, 6)
        buffer.setForeground(TerminalColor.RED)
        buffer.insertTextOnLine("t")
        repeat(5) {
            buffer.addNewLine()
        }
        buffer.setCursorPosition(5, 5)
        buffer.insertTextOnLine("6")
        val screenAndScrollback = buffer.getScreenAndScrollback()
        // Check screen and scrollback for the presence of their respective characters
        assertContains(screenAndScrollback, ("\u001B[31;49m6\u001B[0m"), message = "The screen and scrollback display is incomplete")
        assertContains(screenAndScrollback, ("\u001B[31;49mt\u001B[0m"), message = "The screen and scrollback display is incomplete")
    }

}