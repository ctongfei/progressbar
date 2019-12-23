package me.tongfei.progressbar;

/**
 * A consumer that prints a rendered progress bar.
 * @since 0.8.0
 * @author Alex Peelman
 * @author Tongfei Chen
 */
public interface ProgressBarConsumer extends AutoCloseable {

    /**
     * Event that is triggered before passing the new rendered form of a progress bar.
     */
    void beforeUpdate();

    /**
     * Returns the maximum length allowed for the rendered form of a progress bar.
     */
    int getMaxProgressLength();

    /**
     * Accepts a rendered form of a progress bar, e.g., prints to a specified stream.
     * @param rendered Rendered form of a progress bar, a string
     */
    void accept(String rendered);

    void close();

}
