package me.tongfei.progressbar;

import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static me.tongfei.progressbar.TerminalUtils.*;

/**
 * Progress bar consumer for terminals supporting moving cursor up/down.
 *
 * @author Martin Vehovsky
 */
public class InteractiveConsoleProgressBarConsumer extends ConsoleProgressBarConsumer {

    private boolean initialized = false;
    int position = 1;
    int childOffset = 1;

    public InteractiveConsoleProgressBarConsumer(PrintStream out) {
        super(out);
    }

    @Override
    public void accept(List<String> str) {
        if (!initialized) {
            TerminalUtils.filterActiveConsumers(InteractiveConsoleProgressBarConsumer.class).forEach(c -> c.position++);
            TerminalUtils.activeConsumers.add(this);
            out.println(MOVE_CURSOR_TO_LINE_START + str.get(0));
            initialized = true;
            return;
        }
        if (str.size() > childOffset) {
            int offset = str.size() - childOffset;
            childOffset += offset;
            position += offset;
            AtomicReference<Boolean> takeWhile = new AtomicReference<>(true);
            TerminalUtils.filterActiveConsumers(InteractiveConsoleProgressBarConsumer.class).forEach(consumer -> {
                if (consumer == this) takeWhile.set(false);
                if (takeWhile.get()) consumer.position += offset;
            });
            for (int i = 0; i < offset; i++) {
                out.println(MOVE_CURSOR_TO_LINE_START);
            }
        }

        for (int i = str.size() - 1; i >= 0; i--) {
            out.print(moveCursorUp(position - i) + str.get(i) + moveCursorDown(position - i));
        }
    }

    @Override
    public void close() {
        out.flush();
        TerminalUtils.activeConsumers.remove(this);
    }
}
