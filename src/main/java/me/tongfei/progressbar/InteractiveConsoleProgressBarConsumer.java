package me.tongfei.progressbar;

import java.io.PrintStream;

import static me.tongfei.progressbar.TerminalUtils.*;

/**
 * Progress bar consumer for terminals supporting moving cursor up/down.
 *
 * @author Martin Vehovsky
 */
public class InteractiveConsoleProgressBarConsumer extends ConsoleProgressBarConsumer {

    private boolean initialized = false;
    int position = 1;

    public InteractiveConsoleProgressBarConsumer(PrintStream out) {
        super(out);
    }

    @Override
    public void accept(String str) {
        if (!initialized) {
            TerminalUtils.filterActiveConsumers(InteractiveConsoleProgressBarConsumer.class).forEach(c -> c.position++);
            TerminalUtils.activeConsumers.add(this);
            out.println(MOVE_CURSOR_TO_LINE_START + str);
            initialized = true;
            return;
        }

        out.print(moveCursorUp(position) + str + moveCursorDown(position));
    }

    @Override
    public void close() {
        out.flush();
        TerminalUtils.activeConsumers.remove(this);
    }
}
