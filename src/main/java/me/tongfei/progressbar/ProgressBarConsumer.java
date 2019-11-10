package me.tongfei.progressbar;

public interface ProgressBarConsumer {

    /**
     * method that runs before passing the new state of the progress bar
     */
    void beforeUpdate();

    /**
     * If any output or max limitations are applicable, return the maximum allowed suffix length.
     * @see ConsoleLogger
     *
     * @param prefixLength
     * @return
     */
    int getMaxSuffixLength(int prefixLength);

    /**
     * Max length of the progress bar
     *
     * @return
     */
    int getMaxProgressLength();

    /**
     *
     * @param progressBar The current progress bar state
     */
    void accept(String progressBar);

    void close();
}
