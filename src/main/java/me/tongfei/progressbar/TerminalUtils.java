package me.tongfei.progressbar;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import static org.jline.utils.InfoCmp.Capability.cursor_down;
import static org.jline.utils.InfoCmp.Capability.cursor_up;

/**
 * @author Martin Vehovsky
 * @since 0.9.0
 */
public class TerminalUtils {

    public static final char MOVE_CURSOR_TO_LINE_START = '\r';

    private static int defaultTerminalWidth = 80;
    private static Boolean cursorMovementSupported = null;

    public static Queue<ProgressBarConsumer> activeConsumers = new ConcurrentLinkedQueue<>();

    synchronized public static int getTerminalWidth() {
        Terminal terminal = getTerminal();

        if (terminal != null) {
            int width = terminal.getWidth();
            close(terminal);

            // Workaround for issue #23 under IntelliJ
            if (width >= 10) {
                return width;
            }
        }

        return defaultTerminalWidth;
    }

    synchronized public static boolean cursorMovementSupport() {
        if (cursorMovementSupported == null) {
            Terminal terminal = getTerminal();
            if (terminal != null) {
                cursorMovementSupported = terminal.getStringCapability(cursor_up) != null && terminal.getStringCapability(cursor_down) != null;
                close(terminal);
            } else {
                cursorMovementSupported = false;
            }
        }
        return cursorMovementSupported;
    }

    public static <T extends ProgressBarConsumer> Stream<T> filterActiveConsumers(Class<T> clazz) {
        return activeConsumers.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast);
    }

    public static String moveCursorUp(int count) {
        return String.format("\u001b[%sA", count) + MOVE_CURSOR_TO_LINE_START;
    }

    public static String moveCursorDown(int count) {
        return String.format("\u001b[%sB", count) + MOVE_CURSOR_TO_LINE_START;
    }

    static void close(Terminal terminal) {
        try {
            terminal.close();
        } catch (IOException ignored) { /* noop */ }
    }

    static Terminal getTerminal() {
        try {
            // Issue #42
            // Defaulting to a dumb terminal when a supported terminal can not be correctly created
            // see https://github.com/jline/jline3/issues/291
            return TerminalBuilder.builder().dumb(true).build();
        } catch (IOException e) {
            return null;
        }
    }

}
