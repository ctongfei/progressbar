package me.tongfei.progressbar;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Encapsulates the internal states of a progress bar.
 * @author Tongfei Chen
 * @since 0.5.0
 */
class ProgressState {

    String task;
    boolean indefinite = false;
    long current = 0;
    long max = 0;
    Instant startTime = null;
    String extraMessage = "";
    long start = 0;
    Duration elapsed = Duration.ofSeconds(0L);

    ProgressState(String task, long initialMax, long startFrom, Duration elapsed) {
        this.task = task;
        this.max = initialMax;
        if (initialMax < 0) indefinite = true;
        this.start = startFrom;
        this.current += startFrom;
        this.elapsed = elapsed;
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

    String getTask() {
        return task;
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
        return ((double)current) / max;
    }

}
