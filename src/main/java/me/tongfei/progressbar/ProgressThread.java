package me.tongfei.progressbar;

import java.io.PrintStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * @author Tongfei Chen
 * @since 0.5.0
 */
class ProgressThread implements Runnable {

    ProgressState progress;
    ProgressBarRenderer renderer;
    long updateInterval;
    ProgressBarConsumer consumer;

    ProgressThread(
            ProgressState progress,
            ProgressBarRenderer renderer,
            long updateInterval,
            ProgressBarConsumer consumer
    ) {
        this.progress = progress;
        this.renderer = renderer;
        this.updateInterval = updateInterval;
        this.consumer = consumer;
    }

    private void refresh() {
        consumer.beforeUpdate();
        String rendered = renderer.render(progress, consumer.getMaxProgressLength());
        consumer.accept(rendered);
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                refresh();
                Thread.sleep(updateInterval);
            }
        } catch (InterruptedException ignored) {
            refresh();
            // force refreshing after being interrupted
        }
    }

}
