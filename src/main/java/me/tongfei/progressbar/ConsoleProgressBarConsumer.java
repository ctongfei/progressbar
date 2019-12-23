package me.tongfei.progressbar;

import org.jline.terminal.Terminal;

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
    private Terminal terminal = Util.getTerminal();

    ConsoleProgressBarConsumer() {
        this(System.err);
    }

    ConsoleProgressBarConsumer(PrintStream out) {
        this.out = out;
    }

    @Override
    public int getMaxProgressLength() {
        return Util.getTerminalWidth(terminal) - consoleRightMargin;
    }

    @Override
    public void accept(String str) {
        out.print('\r'); // before update
        out.print(str);
    }

    @Override
    public void close() {
        out.println();
        out.flush();
        try {
            terminal.close();
        }
        catch (IOException ignored) { /* noop */ }
    }
}
