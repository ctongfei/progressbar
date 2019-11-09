package me.tongfei.progressbar;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.PrintStream;

public class ConsoleLogger implements ProgressBarConsumer {

    private static int consoleRightMargin = 2;
    private static int DEFAULT_CONSOLE_WIDTH = 80;
    final PrintStream out;
    int consoleWidth;

    public ConsoleLogger() {
        this(System.err);
    }

    public ConsoleLogger(PrintStream out) {
        this(System.err, DEFAULT_CONSOLE_WIDTH);
    }

    public ConsoleLogger(PrintStream out, int consoleWidth) {
        this.out = out;
        this.consoleWidth = consoleWidth;

        Terminal terminal = null;
        try {
            // Issue #42
            // Defaulting to a dumb terminal when a supported terminal can not be correctly created
            // see https://github.com/jline/jline3/issues/291
            terminal = TerminalBuilder.builder().dumb(true).build();
        }
        catch (IOException ignored) { }

        if (terminal != null && terminal.getWidth() >= 10)  // Workaround for issue #23 under IntelliJ
            consoleWidth = terminal.getWidth();
    }

    @Override
    public void beforeUpdate() {
        out.print('\r');
    }

    @Override
    public int getMaxSuffixLength(int prefixLength) {
        return Math.max(0, consoleWidth - consoleRightMargin - prefixLength - 10);
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
