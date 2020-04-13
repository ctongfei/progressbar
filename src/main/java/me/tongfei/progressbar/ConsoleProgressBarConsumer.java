package me.tongfei.progressbar;

import java.io.PrintStream;
import java.util.List;

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
    public void accept(List<String> str) {
        out.print(MOVE_CURSOR_TO_LINE_START + str.get(0));
    }

    @Override
    public void close() {
        out.println();
        out.flush();
    }
}
