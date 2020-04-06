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

    private static int consoleRightMargin = 2;
    private final PrintStream out;

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
                    int currentPosition = Util.currentCursorPosition();
                    moveCursorUp(currentPosition - position);
                    replaceLine(str);
                    moveCursorDown(currentPosition - position);
                } else {
                    position = Util.currentCursorPosition();

                    // prevent issues caused by reaching terminal height => multiple progressbars having same (maximum) cursor position
                    if (position == Util.getTerminal().getHeight() - 1) {
                        Util.terminalConsumers.forEach(c -> {
                            c.position--;
                        });
                    }

                    out.print('\r');
                    out.println(str);
                    initialized = true;
                }
            }
        } else {
            replaceLine(str);
        }
    }

    private void moveCursorUp(int count) {
        if (count <= 0) {
            return;
        }
        out.print(String.format("\u001b[%sA", count));
    }

    private void moveCursorDown(int count) {
        if (count <= 0) {
            return;
        }
        out.print(String.format("\u001b[%sB", count));
    }

    private void replaceLine(String str) {
        out.print('\r');
        out.print(str);
        out.print('\r');
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
