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
    private static int defaultConsoleWidth = 80;
    private final PrintStream out;
    private int consoleWidth = defaultConsoleWidth;

    ConsoleProgressBarConsumer() {
        this(System.err);
    }

    ConsoleProgressBarConsumer(PrintStream out) {
        this.out = out;

        Terminal terminal = null;
        try {
            // Issue #42
            // Defaulting to a dumb terminal when a supported terminal can not be correctly created
            // see https://github.com/jline/jline3/issues/291
            terminal = TerminalBuilder.builder().dumb(true).build();
        }
        catch (IOException ignored) { }

        if (terminal != null && terminal.getWidth() >= 10) { // Workaround for issue #23 under IntelliJ
            this.consoleWidth = terminal.getWidth();

            try {
                terminal.close();
            } catch (IOException e) {
                //NOOP
            }
        }
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
        out.close();
    }
}
