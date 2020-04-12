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
    public static final char ESCAPE_CHAR = '\u001b';

    private static final int defaultTerminalWidth = 80;
    private static Boolean cursorMovementSupported = null;

    private static Terminal terminal = null;
    public static Queue<ProgressBarConsumer> activeConsumers = new ConcurrentLinkedQueue<>();

    synchronized public static int getTerminalWidth() {
        Terminal terminal = getTerminal();
        int width = terminal.getWidth();

        // Workaround for issue #23 under IntelliJ
        if (width >= 10) {
            return width;
        }

        return defaultTerminalWidth;
    }

    synchronized public static boolean cursorMovementSupport() {
        if (cursorMovementSupported == null) {
            Terminal terminal = getTerminal();
            cursorMovementSupported = terminal.getStringCapability(cursor_up) != null && terminal.getStringCapability(cursor_down) != null;
            closeTerminal();
        }
        return cursorMovementSupported;
    }

    synchronized static void closeTerminal() {
        try {
            terminal.close();
            terminal = null;
        } catch (IOException ignored) { /* noop */ }
    }

    public static <T extends ProgressBarConsumer> Stream<T> filterActiveConsumers(Class<T> clazz) {
        return activeConsumers.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast);
    }

    public static String moveCursorUp(int count) {
        return String.format(ESCAPE_CHAR + "[%sA", count) + MOVE_CURSOR_TO_LINE_START;
    }

    public static String moveCursorDown(int count) {
        return String.format(ESCAPE_CHAR + "[%sB", count) + MOVE_CURSOR_TO_LINE_START;
    }

    /**
     * <ul>
     *     <li>Creating terminal is relatively expensive, usually takes between 5-10ms.
     *         <ul>
     *             <li>If updateInterval is set under 10ms creating new terminal for on every re-render of progress bar could be a problem.</li>
     *             <li>Especially when multiple progress bars are running in parallel.</li>
     *         </ul>
     *     </li>
     *     <li>Another problem with {@link Terminal} is that once created you can create another instance (say from different thread), but this instance will be
     *     "dumb". Until previously created terminal will be closed.
     *     </li>
     * </ul>
     *
     * @return
     */
    static Terminal getTerminal() {
        if (terminal == null) {
            try {
                // Issue #42
                // Defaulting to a dumb terminal when a supported terminal can not be correctly created
                // see https://github.com/jline/jline3/issues/291
                terminal = TerminalBuilder.builder().dumb(true).build();
            } catch (IOException e) {
                throw new RuntimeException("This should never happen! Dump terminal should have been created.");
            }
        }
        return terminal;
    }

}
