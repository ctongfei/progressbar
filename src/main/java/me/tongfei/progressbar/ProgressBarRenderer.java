package me.tongfei.progressbar;

/**
 * Renders a {@link ProgressState} into a string.
 * @author Tongfei Chen
 * @since 0.8.0
 */
@FunctionalInterface
public interface ProgressBarRenderer {

    /**
     * Renders the current progress bar state as a string to be shown by a consumer.
     * @param progress The current progress bar state
     * @param maxLength The maximum length as dictated by the consumer
     * @return Rendered string to be consumed by the consumer
     */
    String render(ProgressState progress, int maxLength);

}
