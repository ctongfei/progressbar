package me.tongfei.progressbar;

import java.io.PrintStream;

import static me.tongfei.progressbar.TerminalUtils.MOVE_CURSOR_TO_LINE_START;

/**
 * Progress bar consumer that prints the progress bar state to console.
 * By default {@link System#err} is used as {@link PrintStream}.
 *
 * @author Tongfei Chen
 * @author Alex Peelman
 */
public class ConsoleProgressBarConsumer implements ProgressBarConsumer {

    private static int consoleRightMargin = 2;
    final PrintStream out;

    public ConsoleProgressBarConsumer(PrintStream out) {
        this.out = out;
    }

    @Override
    public int getMaxProgressLength() {
        return TerminalUtils.getTerminalWidth() - consoleRightMargin;
    }

    @Override
    public void accept(String str) {
        out.print(MOVE_CURSOR_TO_LINE_START + str);
    }

    @Override
    public void close() {
        out.println();
        out.flush();
    }
}
