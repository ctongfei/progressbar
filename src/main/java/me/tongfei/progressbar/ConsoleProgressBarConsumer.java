package me.tongfei.progressbar;

import java.io.PrintStream;

/**
 * Progress bar consumer that prints the progress bar state to console.
 * By default {@link System#err} is used as {@link PrintStream}.
 *
 * @author Tongfei Chen
 * @author Alex Peelman
 */
public class ConsoleProgressBarConsumer implements ProgressBarConsumer {

    private static final char MOVE_CURSOR_TO_LINE_START = '\r';
    private static int consoleRightMargin = 2;
    final PrintStream out;

    private boolean initialized = false;
    int position = -1;

    ConsoleProgressBarConsumer() {
        this(System.err);
    }

    ConsoleProgressBarConsumer(PrintStream out) {
        this.out = out;
        Util.terminalConsumers.add(this);
    }

    @Override
    public int getMaxProgressLength() {
        return Util.getTerminalWidth() - consoleRightMargin;
    }

    @Override
    public void accept(String str) {
        if (Util.cursorMovementSupport()) {
            synchronized (out) {
                if (initialized) {
                    out.print(moveCursorUp(position) + str + moveCursorDown(position));
                } else {
                    position = 0;
                    Util.terminalConsumers.forEach(c -> {
                        if (c.position != -1) {
                            c.position++;
                        }
                    });

                    out.println(MOVE_CURSOR_TO_LINE_START + str);
                    initialized = true;
                }
            }
        } else {
            out.print(MOVE_CURSOR_TO_LINE_START + str);
        }
    }

    private String moveCursorUp(int count) {
        if (count <= 0) {
            return "";
        }
        return String.format("\u001b[%sA", count) + MOVE_CURSOR_TO_LINE_START;
    }

    private String moveCursorDown(int count) {
        if (count <= 0) {
            return "";
        }
        return String.format("\u001b[%sB", count) + MOVE_CURSOR_TO_LINE_START;
    }

    @Override
    public void close() {
        if (!Util.cursorMovementSupport()) {
            out.println();
        }
        out.flush();
        Util.terminalConsumers.remove(this);
        Util.closeTerminal();
    }
}
