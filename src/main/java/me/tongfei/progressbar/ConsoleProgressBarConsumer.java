package me.tongfei.progressbar;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Progress bar consumer that prints the progress bar state to console.
 * By default {@link System#err} is used as {@link PrintStream}.
 * @author Tongfei Chen
 * @author Alex Peelman
 */
public class ConsoleProgressBarConsumer implements ProgressBarConsumer {

    private static int consoleRightMargin = 2;
    private final PrintStream out;
    private int consoleWidth = Util.getTerminalWidth();

    ConsoleProgressBarConsumer() {
        this(System.err);
    }

    ConsoleProgressBarConsumer(PrintStream out) {
        this.out = out;
    }

    @Override
    public void beforeUpdate() {
        out.print('\r');
    }

    @Override
    public int getMaxProgressLength() {
        return consoleWidth - consoleRightMargin;
    }

    @Override
    public void accept(String progressBar) {
        out.print(progressBar);
    }

    @Override
    public void close() {
        out.println();
        out.flush();
    }
}
