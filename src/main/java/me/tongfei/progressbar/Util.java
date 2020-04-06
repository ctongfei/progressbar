package me.tongfei.progressbar;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.jline.terminal.Cursor;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

/**
 * @author Tongfei Chen
 * @since 0.5.0
 */
class Util {

    static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, runnable -> {
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setName("tongfei.progressbar");
        thread.setDaemon(true);
        return thread;
    });

    static List<ConsoleProgressBarConsumer> terminalConsumers = Collections.synchronizedList(new ArrayList<>());

    private static int defaultTerminalWidth = 80;
    private static boolean cursorMovementSupport = false;
    private static Terminal terminal = null;

    static String repeat(char c, int n) {
        if (n <= 0) return "";
        char[] s = new char[n];
        for (int i = 0; i < n; i++) s[i] = c;
        return new String(s);
    }

    static String formatDuration(Duration d) {
        long s = d.getSeconds();
        return String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60);
    }

    static long getInputStreamSize(InputStream is) {
        try {
            if (is instanceof FileInputStream)
                return ((FileInputStream) is).getChannel().size();
        } catch (IOException e) {
            return -1;
        }
        return -1;
    }

    static boolean cursorMovementSupport() {
        getTerminal();
        return cursorMovementSupport;
    }

    static void closeTerminal() {
        if (terminalConsumers.size() == 0) {
            try {
                terminal.close();
            } catch (IOException ignored) { /* noop */ }
            terminal = null;
            cursorMovementSupport = false;
        }
    }

    static Terminal getTerminal() {
        if (terminal == null) {
            try {
                // Issue #42
                // Defaulting to a dumb terminal when a supported terminal can not be correctly created
                // see https://github.com/jline/jline3/issues/291
                terminal = TerminalBuilder.builder().dumb(true).build();

                if (!terminal.getType().equals(Terminal.TYPE_DUMB)) {
                    terminal.enterRawMode();
                }
            } catch (IOException ignored) {
            }
            cursorMovementSupport = terminal.getStringCapability(InfoCmp.Capability.cursor_up) != null
                    && terminal.getStringCapability(InfoCmp.Capability.cursor_down) != null;
        }
        return terminal;
    }

    static int currentCursorPosition() {
        Cursor cursor = getTerminal().getCursorPosition(value -> {
            //FIXME: what to do about discarded characters??
        });
        if (cursor == null) {
            return 0;
        }
        return cursor.getY();
    }

    static int getTerminalWidth(Terminal terminal) {
        if (terminal != null && terminal.getWidth() >= 10) // Workaround for issue #23 under IntelliJ
            return terminal.getWidth();
        else return defaultTerminalWidth;
    }

    static int getTerminalWidth() {
        Terminal terminal = getTerminal();
        return getTerminalWidth(terminal);
    }

}
