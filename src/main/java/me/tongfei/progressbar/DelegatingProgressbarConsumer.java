package me.tongfei.progressbar;

import java.util.function.Consumer;

/**
 * Progress bar consumer that delegates the progress bar handling to a custom {@link Consumer}
 */
public class DelegatingProgressbarConsumer implements ProgressBarConsumer {

    private final int maxProgressLength;
    private final Consumer<String> progressBarConsumer;

    public DelegatingProgressbarConsumer(Consumer<String> progressBarConsumer) {
        this(progressBarConsumer, 80);
    }

    public DelegatingProgressbarConsumer(Consumer<String> progressBarConsumer, int maxProgressLength) {
        this.maxProgressLength = maxProgressLength;
        this.progressBarConsumer = progressBarConsumer;
    }

    @Override
    public void beforeUpdate() {
        //NOOP
    }

    @Override
    public int getMaxSuffixLength(int prefixLength) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxProgressLength() {
        return maxProgressLength;
    }

    @Override
    public void accept(String progressBar) {
        this.progressBarConsumer.accept(progressBar);
    }

    @Override
    public void close() {
        //NOOP
    }
}
