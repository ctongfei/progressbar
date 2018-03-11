package me.tongfei.progressbar;

import me.tongfei.progressbar.wrapped.ProgressBarWrappedIterable;
import me.tongfei.progressbar.wrapped.ProgressBarWrappedIterator;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A simple console-based progress bar.
 * @author Tongfei Chen
 */
public class ProgressBar {

    private ProgressState progress;
    private ProgressThread target;
    private Thread thread;

    /**
     * Creates a progress bar with the specific task name and initial maximum value.
     * @param task Task name
     * @param initialMax Initial maximum value
     */
    public ProgressBar(String task, long initialMax) {
        this(task, initialMax, 1000, System.err, ProgressBarStyle.UNICODE_BLOCK);
    }

    public ProgressBar(String task, long initialMax, ProgressBarStyle style) {
        this(task, initialMax, 1000, System.err, style);
    }

    public ProgressBar(String task, long initialMax, int updateIntervalMillis) {
        this(task, initialMax, updateIntervalMillis, System.err, ProgressBarStyle.UNICODE_BLOCK);
    }

    /**
     * Creates a progress bar with the specific task name, initial maximum value,
     * customized update interval (default 1000 ms), the PrintStream to be used, and output style.
     * @param task Task name
     * @param initialMax Initial maximum value
     * @param updateIntervalMillis Update interval (default value 1000 ms)
     * @param os Print stream (default value System.err)
     * @param style Output style (default value ProgresBarStyle.UNICODE_BLOCK)
     */
    public ProgressBar(String task, long initialMax, int updateIntervalMillis, PrintStream os, ProgressBarStyle style) {
        this.progress = new ProgressState(task, initialMax);
        this.target = new ProgressThread(progress, style, updateIntervalMillis, os);
        this.thread = new Thread(target);
    }

    /**
     * Starts this progress bar.
     */
    public ProgressBar start() {
        progress.startTime = LocalDateTime.now();
        thread.start();
        return this;
    }

    /**
     * Advances this progress bar by a specific amount.
     * @param n Step size
     */
    public ProgressBar stepBy(long n) {
        progress.stepBy(n);
        return this;
    }

    /**
     * Advances this progress bar to the specific progress value.
     * @param n New progress value
     */
    public ProgressBar stepTo(long n) {
        progress.stepTo(n);
        return this;
    }

    /**
     * Advances this progress bar by one step.
     */
    public ProgressBar step() {
        progress.stepBy(1);
        return this;
    }

    /**
     * Gives a hint to the maximum value of the progress bar.
     * @param n Hint of the maximum value
     */
    public ProgressBar maxHint(long n) {
        if (n < 0)
            progress.setAsIndefinite();
        else {
            progress.setAsDefinite();
            progress.maxHint(n);
        }
        return this;
    }

    /**
     * Stops this progress bar.
     */
    public ProgressBar stop() {
        target.kill();
        try {
            thread.join();
            target.consoleStream.print("\n");
            target.consoleStream.flush();
        }
        catch (InterruptedException ex) { }
        return this;
    }

    /**
     * Sets the extra message at the end of the progress bar.
     * @param msg New message
     */
    public ProgressBar setExtraMessage(String msg) {
        progress.setExtraMessage(msg);
        return this;
    }

	/**
     * Returns the current progress.
     */
    public long getCurrent() {
        return progress.getCurrent();
    }

    /**
     * Returns the maximum value of this progress bar.
     */
    public long getMax() {
        return progress.getMax();
    }

    /**
     * Returns the name of this task.
     */
    public String getTask() {
        return progress.getTask();
    }

    /**
     * Returns the extra message at the end of the progress bar.
     */
    public String getExtraMessage() {
        return progress.getExtraMessage();
    }

    /**
     * Wraps an iterator so that when iterated, a progress bar is shown to track the traversal progress.
     * @param it Underlying iterator
     * @param task Task name
     */
    public static <T> Iterator<T> wrap(Iterator<T> it, String task) {
        return new ProgressBarWrappedIterator<>(it, task, -1); // indefinite progress bar
    }

    /**
     * Wraps an iterable so that when iterated, a progress bar is shown to track the traversal progress.
     * <p>
     * Sample usage: {@code
     *   for (T x : ProgressBar.wrap(collection, "Traversal")) { ... }
     * }
     * </p>
     * @param ts Underlying iterable
     * @param task Task name
     */
    public static <T> Iterable<T> wrap(Iterable<T> ts, String task) {
        return new ProgressBarWrappedIterable<>(ts, task);
    }

}
