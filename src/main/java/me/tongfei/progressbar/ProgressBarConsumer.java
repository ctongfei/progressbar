package me.tongfei.progressbar;

import java.util.function.Consumer;

/**
 * A consumer that prints a rendered progress bar.
 * @since 0.8.0
 * @author Alex Peelman
 * @author Tongfei Chen
 */
public interface ProgressBarConsumer extends Consumer<String>, Appendable, AutoCloseable {

    /**
     * Returns the maximum length allowed for the rendered form of a progress bar.
     */
    int getMaxProgressLength();

    /**
     * Accepts a rendered form of a progress bar, e.g., prints to a specified stream.
     * @param rendered Rendered form of a progress bar, a string
     */
    void accept(String rendered);

    default ProgressBarConsumer append(CharSequence csq) {
        accept(csq.toString());
        return this;
    }

    default ProgressBarConsumer append(CharSequence csq, int start, int end) {
        accept(csq.subSequence(start, end).toString());
        return this;
    }

    default ProgressBarConsumer append(char c) {
        accept(String.valueOf(c));
        return this;
    }

    void close();

}
