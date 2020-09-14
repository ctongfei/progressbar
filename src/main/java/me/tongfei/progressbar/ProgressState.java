package me.tongfei.progressbar;

import java.time.Duration;
import java.time.Instant;

/**
 * Encapsulates the internal states of a progress bar.
 * @author Tongfei Chen
 * @since 0.5.0
 */
class ProgressState {

    String taskName;
    String extraMessage = "";

    boolean indefinite = false;

    //  0             start     current        max
    //  [===============|=========>             ]
    long start;
    long current;
    long max;

    Instant startInstant = null;
    Duration elapsedBeforeStart = Duration.ZERO;

    volatile boolean alive = true;
    volatile boolean paused = false;

    ProgressState(String taskName, long initialMax, long startFrom, Duration elapsedBeforeStart) {
        this.taskName = taskName;
        this.max = initialMax;
        if (initialMax < 0) indefinite = true;
        this.start = startFrom;
        this.current = startFrom;
        this.elapsedBeforeStart = elapsedBeforeStart;
        this.startInstant = Instant.now();
    }

    String getTaskName() {
        return taskName;
    }

    synchronized String getExtraMessage() {
        return extraMessage;
    }

    synchronized long getCurrent() {
        return current;
    }

    synchronized long getMax() {
        return max;
    }

    // The progress, normalized to range [0, 1].
    synchronized double getNormalizedProgress() {
        if (max <= 0) return 0.0;
        else if (current > max) return 1.0;
        else return ((double)current) / max;
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

    synchronized void kill() {
        alive = false;
    }

}
