package me.tongfei.progressbar;

import java.time.Duration;
import java.time.Instant;

/**
 * Encapsulates the internal states of a progress bar.
 * @author Tongfei Chen
 * @since 0.5.0
 */
public class ProgressState {

    String taskName;
    String extraMessage = "";

    boolean indefinite = false;

    //  0             start     current        max
    //  [===============|=========>             ]
    long start;
    long current;
    long max;

    Instant startInstant;
    Duration elapsedBeforeStart;

    volatile boolean alive = true;
    volatile boolean paused = false;

    ProgressState(String taskName, long initialMax, long startFrom, Duration elapsedBeforeStart) {
        this.taskName = taskName;
        if (initialMax < 0)
            indefinite = true;
        else this.max = initialMax;
        this.start = startFrom;
        this.current = startFrom;

        this.startInstant = Instant.now();
        this.elapsedBeforeStart = elapsedBeforeStart;
    }

    public String getTaskName() {
        return taskName;
    }

    public synchronized String getExtraMessage() {
        return extraMessage;
    }

    public synchronized long getStart() {
        return start;
    }

    public synchronized long getCurrent() {
        return current;
    }

    public synchronized long getMax() {
        return max;
    }

    public synchronized double getNormalizedProgress() {
        if (max <= 0) return 0.0;
        else if (current > max) return 1.0;
        else return ((double)current) / max;
    }

    public synchronized Instant getStartInstant() {
        return startInstant;
    }

    public synchronized Duration getElapsedBeforeStart() {
        return elapsedBeforeStart;
    }

    public synchronized Duration getElapsedAfterStart() {
        return (startInstant == null)
                ? Duration.ZERO
                : Duration.between(startInstant, Instant.now());
    }

    public synchronized Duration getTotalElapsed() {
        return getElapsedBeforeStart().plus(getElapsedAfterStart());
    }

    public synchronized boolean isIndefinite() {
        return indefinite;
    }

    public synchronized boolean isAlive() {
        return alive;
    }

    public synchronized boolean isPaused() {
        return paused;
    }

    synchronized void setAsDefinite() {
        indefinite = false;
    }

    synchronized void setAsIndefinite() {
        indefinite = true;
    }

    synchronized void maxHint(long n) {
        max = n;
    }

    synchronized void stepBy(long n) {
        current += n;
        if (current > max) max = current;
    }

    synchronized void stepTo(long n) {
        current = n;
        if (current > max) max = current;
    }

    synchronized void setExtraMessage(String msg) {
        extraMessage = msg;
    }

    synchronized void pause() {
        paused = true;
        start = current;
        elapsedBeforeStart = elapsedBeforeStart.plus(Duration.between(startInstant, Instant.now()));
    }

    synchronized void resume() {
        paused = false;
        startInstant = Instant.now();
    }

    synchronized void reset() {
        start = 0;
        current = 0;
        startInstant = Instant.now();
        elapsedBeforeStart = Duration.ZERO;
    }

    synchronized void kill() {
        alive = false;
    }

}
