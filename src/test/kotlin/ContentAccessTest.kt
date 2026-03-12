import com.internship.buffer.TerminalBuffer
import com.internship.types.Direction
import com.internship.types.TerminalColor
import com.internship.types.TextStyle
import kotlin.test.Test
import kotlin.test.assertContains


class ContentAccessTest {
    @Test
    fun getCharacterAtPositionTest() {
        val buffer = TerminalBuffer(80 to 25)
        buffer.setCursorPosition(20, 20)
        buffer.writeTextOnLine('a')
        val char1 = buffer.getScreenCharacterAtPosition(20, 20)
        assert(char1 == 'a') {
            "Wrong character has been returned from screen, expected 'a', but got '${char1}'"
        }
        buffer.setCursorPosition(5, 0)
        buffer.writeTextOnLine('b')
        buffer.addNewLine()
        val char2 = buffer.getScrollbackCharacterAtPosition(5, 0)
        assert(char2 == 'b') {
            "Wrong character has been returned from scrollback, expected 'b', but got '${char2}'"
        }
    }

    @Test
    fun getAttributeAtPositionTest() {
        val buffer = TerminalBuffer(80 to 25)
        buffer.setAttribute(TextStyle.BOLD, true)
        buffer.setForeground(TerminalColor.WHITE)
        buffer.setBackground(TerminalColor.BLUE)
        buffer.setCursorPosition(20, 20)
        buffer.writeTextOnLine('x')
        val attrs1 = buffer.getScreenAttributesAtPosition(20, 20)
        assert(attrs1.first.contains(TextStyle.BOLD) && attrs1.second == TerminalColor.WHITE && attrs1.third == TerminalColor.BLUE) {
            "Style changes have not been applied on screen during character insertion"
        }
        buffer.setAttribute(TextStyle.UNDERLINE, true)
        buffer.setForeground(TerminalColor.YELLOW)
        buffer.setBackground(TerminalColor.GREEN)
        buffer.setCursorPosition(5, 0)
        buffer.writeTextOnLine('x')
        buffer.addNewLine()
        val attrs2 = buffer.getScrollbackAttributesAtPosition(5, 0)
        assert(attrs2.first.contains(TextStyle.UNDERLINE) && attrs2.second == TerminalColor.YELLOW && attrs2.third == TerminalColor.GREEN) {
            "Style changes have not been applied on scrollback during character insertion"
        }
    }

    @Test
    fun getLineAsStringTest() {
        val width = 5
        val buffer = TerminalBuffer(width to 10)
        buffer.moveCursor(Direction.DOWN, 2)
        buffer.fillLine('s')
        val line1 = buffer.getScreenLine(2)
        assert(line1 == "s".repeat(width)) {
            "Screen line getter returned invalid value, expected sssss, but got '${line1}'"
        }
        repeat(4) {
            buffer.addNewLine()
        }
        val line2 = buffer.getScrollbackLine(1)
        assert(line2 == "s".repeat(width)) {
            "Scrollback line getter returned invalid value, expected sssss, but got '${line2}'"
        }
    }

    @Test
    fun getEntireScreenTest() {
        val buffer = TerminalBuffer(6 to 6)
        buffer.setForeground(TerminalColor.RED)
        buffer.moveCursor(Direction.DOWN, 1)
        buffer.insertTextOnLine('t')
        buffer.setCursorPosition(5, 5)
        buffer.insertTextOnLine('6')
        val screen = buffer.getScreen()
        assertContains(screen, ("\u001B[31;49m6\u001B[0m"), message = "The screen display is incomplete")
        assertContains(screen, ("\u001B[31;49mt\u001B[0m"), message = "The screen display is incomplete")
    }

    @Test
    fun getEntireScreenAndScrollbackTest() {
        val buffer = TerminalBuffer(6 to 6, 6)
        buffer.setForeground(TerminalColor.RED)
        buffer.insertTextOnLine('t')
        repeat(5) {
            buffer.addNewLine()
        }
        buffer.setCursorPosition(5, 5)
        buffer.insertTextOnLine('6')
        val screen = buffer.getScreenAndScrollback()
        assertContains(screen, ("\u001B[31;49m6\u001B[0m"), message = "The screen and scrollback display is incomplete")
        assertContains(screen, ("\u001B[31;49mt\u001B[0m"), message = "The screen and scrollback display is incomplete")
    }

}