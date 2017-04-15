package me.tongfei.progressbar;

import java.time.LocalDateTime;

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

    LocalDateTime startTime = null;

    String extraMessage = "";

    ProgressState(String task, long initialMax) {
        this.task = task;
        this.max = initialMax;
        if (initialMax < 0) indefinite = true;
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

}
