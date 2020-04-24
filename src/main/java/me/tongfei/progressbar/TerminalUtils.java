package me.tongfei.progressbar;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

/**
 * @author Martin Vehovsky
 * @since 1.0.0
 */
public class TerminalUtils {

    static final char CARRIAGE_RETURN = '\r';
    static final char ESCAPE_CHAR = '\u001b';
    static final int DEFAULT_TERMINAL_WIDTH = 80;

    private static Terminal terminal = null;
    private static boolean cursorMovementSupported = false;

    static Queue<ProgressBarConsumer> activeConsumers = new ConcurrentLinkedQueue<>();

    synchronized static int getTerminalWidth() {
        Terminal terminal = getTerminal();
        int width = terminal.getWidth();

        // Workaround for issue #23 under IntelliJ
        return (width >= 10) ? width : DEFAULT_TERMINAL_WIDTH;
    }

    static boolean hasCursorMovementSupport() {
        if (terminal == null)
            terminal = getTerminal();
        return cursorMovementSupported;
    }

    synchronized static void closeTerminal() {
        try {
            if (terminal != null) {
                terminal.close();
                terminal = null;
            }
        } catch (IOException ignored) { /* noop */ }
    }

    static <T extends ProgressBarConsumer> Stream<T> filterActiveConsumers(Class<T> clazz) {
        return activeConsumers.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast);
    }

    static String moveCursorUp(int count) {
        return ESCAPE_CHAR + "[" + count + "A" + CARRIAGE_RETURN;
    }

    static String moveCursorDown(int count) {
        return ESCAPE_CHAR + "[" + count + "B" + CARRIAGE_RETURN;
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
     */
    static Terminal getTerminal() {
        if (terminal == null) {
            try {
                // Issue #42
                // Defaulting to a dumb terminal when a supported terminal can not be correctly created
                // see https://github.com/jline/jline3/issues/291
                terminal = TerminalBuilder.builder().dumb(true).build();
                cursorMovementSupported = (
                        terminal.getStringCapability(InfoCmp.Capability.cursor_up) != null &&
                        terminal.getStringCapability(InfoCmp.Capability.cursor_down) != null
                );
            } catch (IOException e) {
                throw new RuntimeException("This should never happen! Dumb terminal should have been created.");
            }
        }
        return terminal;
    }

}
