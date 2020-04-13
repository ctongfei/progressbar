package me.tongfei.progressbar;

import java.util.List;
import java.util.function.Consumer;

/**
 * A consumer that prints a rendered progress bar.
 * @since 0.8.0
 * @author Alex Peelman
 * @author Tongfei Chen
 */
public interface ProgressBarConsumer extends Consumer<List<String>>, AutoCloseable {

    /**
     * Returns the maximum length allowed for the rendered form of a progress bar.
     */
    int getMaxProgressLength();

    /**
     * Accepts a rendered form of a progress bar, e.g., prints to a specified stream.
     * @param rendered Rendered form of a progress bar, a string
     */
    void accept(List<String> rendered);

    void close();

}
