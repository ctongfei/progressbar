package me.tongfei.progressbar;

import java.io.PrintStream;
import java.time.LocalDateTime;

/**
 * A simple console-based progress bar.
 * @author Tongfei Chen
 */
public class ProgressBar {

    private static String symbols = " ▏▎▍▌▋▊▉█";

    private Progress progress;
    private ProgressThread target;
    private Thread thread;

    /**
     * Creates a progress bar with the specific task name and initial maximum value.
     * @param task Task name
     * @param initialMax Initial maximum value
     */
    public ProgressBar(String task, int initialMax) {
        this.progress = new Progress(task, initialMax);
        this.target = new ProgressThread(progress, 1000, System.err);
        this.thread = new Thread(target);
    }

    /**
     * Creates a progress bar with the specific task name, initial maximum value
     * and customized update interval (default 1000 ms).
     * @param task Task name
     * @param initialMax Initial maximum value
     * @param updateIntervalMillis Update interval (default value 1000 ms)
     */
    public ProgressBar(String task, int initialMax, int updateIntervalMillis) {
        this.progress = new Progress(task, initialMax);
        this.target = new ProgressThread(progress, updateIntervalMillis, System.err);
        this.thread = new Thread(target);
    }


    /**
     * Creates a progress bar with the specific task name, initial maximum value,
     * customized update interval (default 1000 ms) and the PrintStream to be used.
     * @param task Task name
     * @param initialMax Initial maximum value
     * @param updateIntervalMillis Update interval (default value 1000 ms)
     * @param os Print stream (default value System.err)
     */
    public ProgressBar(String task, int initialMax, int updateIntervalMillis, PrintStream os) {
        this.progress = new Progress(task, initialMax);
        this.target = new ProgressThread(progress, updateIntervalMillis, os);
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
    }

    /**
     * Sets the extra message at the end of the progress bar.
     * @param msg New message
     */
    public void setExtraMessage(String msg) {
        progress.setExtraMessage(msg);
    }

}
