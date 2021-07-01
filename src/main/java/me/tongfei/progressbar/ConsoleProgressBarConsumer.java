package me.tongfei.progressbar;

import java.io.PrintStream;

import static me.tongfei.progressbar.TerminalUtils.CARRIAGE_RETURN;

/**
 * Progress bar consumer that prints the progress bar state to console.
 * By default {@link System#err} is used as {@link PrintStream}.
 *
 * @author Tongfei Chen
 * @author Alex Peelman
 */
public class ConsoleProgressBarConsumer implements ProgressBarConsumer {

    private static int consoleRightMargin = 1;
    int maxRenderedLength = -1;
    final PrintStream out;

    public ConsoleProgressBarConsumer(PrintStream out) {
        this.out = out;
    }

    public ConsoleProgressBarConsumer(PrintStream out, int maxRenderedLength) {
        this.maxRenderedLength = maxRenderedLength;
        this.out = out;
    }

    @Override
    public int getMaxRenderedLength() {
        if (maxRenderedLength <= 0)
            return TerminalUtils.getTerminalWidth() - consoleRightMargin;
        else return maxRenderedLength;
    }

    @Override
    public void accept(String str) {
        int displayLength = StringDisplayUtils.getStringDisplayLength(str);
        int acceptedLength = Math.min(displayLength, getMaxRenderedLength());
        out.print(CARRIAGE_RETURN + StringDisplayUtils.trimDisplayLength(str, acceptedLength));
    }

    @Override
    public void close() {
        out.println();
        out.flush();
    }
}
