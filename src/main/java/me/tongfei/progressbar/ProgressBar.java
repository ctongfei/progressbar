package me.tongfei.progressbar;

import java.io.PrintStream;
import java.time.LocalDateTime;

/**
 * A simple console-based progress bar.
 * @author Tongfei Chen
 */
public class ProgressBar {


    private ProgressBarStyle style;
    private ProgressState progress;
    private ProgressThread target;
    private Thread thread;

    /**
     * Creates a progress bar with the specific task name and initial maximum value.
     * @param task Task name
     * @param initialMax Initial maximum value
     */
    public ProgressBar(String task, int initialMax) {
        this(task, initialMax, 1000, System.err, ProgressBarStyle.UNICODE_BLOCK);
    }

    public ProgressBar(String task, int initialMax, ProgressBarStyle style) {
        this(task, initialMax, 1000, System.err, style);
    }

    public ProgressBar(String task, int initialMax, int updateIntervalMillis) {
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
    public ProgressBar(String task, int initialMax, int updateIntervalMillis, PrintStream os, ProgressBarStyle style) {
        this.progress = new ProgressState(task, initialMax);
        this.target = new ProgressThread(progress, style, updateIntervalMillis, os);
        this.thread = new Thread(target);
    }

    /**
     * Starts this progress bar.
     */
    public void start() {
        progress.startTime = LocalDateTime.now();
        thread.start();
    }

    /**
     * Advances this progress bar by a specific amount.
     * @param n Step size
     */
    public void stepBy(int n) {
        progress.stepBy(n);
    }

    /**
     * Advances this progress bar to the specific progress value.
     * @param n New progress value
     */
    public void stepTo(int n) {
        progress.stepTo(n);
    }

    /**
     * Advances this progress bar by one step.
     */
    public void step() {
        progress.stepBy(1);
    }

    /**
     * Gives a hint to the maximum value of the progress bar.
     * @param n Hint of the maximum value
     */
    public void maxHint(int n) {
        progress.maxHint(n);
    }

    /**
     * Stops this progress bar.
     */
    public void stop() {
        target.kill();
        try {
            thread.join();
            target.consoleStream.print("\n");
            target.consoleStream.flush();
        }
        catch (InterruptedException ex) { }
    }

    /**
     * Sets the extra message at the end of the progress bar.
     * @param msg New message
     */
    public void setExtraMessage(String msg) {
        progress.setExtraMessage(msg);
    }

	/**
     * Returns the current progress.
     */
    public int getCurrent() {
        return progress.getCurrent();
    }

    /**
     * Returns the maximum value of this progress bar.
     */
    public int getMax() {
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

}
