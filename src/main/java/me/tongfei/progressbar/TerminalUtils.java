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
class TerminalUtils {

    private static int defaultTerminalWidth = 80;

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
        Terminal terminal = getTerminal();
        boolean cursorMovementSupported = false;
        if (terminal != null) {
            cursorMovementSupported = terminal.getStringCapability(cursor_up) != null && terminal.getStringCapability(cursor_down) != null;
            close(terminal);
        }
        return cursorMovementSupported;
    }

    public static <T extends ProgressBarConsumer> Stream<T> filterActiveConsumers(Class<T> clazz) {
        return activeConsumers.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast);
    }

    private static void close(Terminal terminal) {
        try {
            terminal.close();
        } catch (IOException ignored) { /* noop */ }
    }

    private static Terminal getTerminal() {
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
