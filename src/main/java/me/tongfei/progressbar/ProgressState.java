package me.tongfei.progressbar;

import java.time.LocalDateTime;

/**
 * Encapsulates the internal states of a progress bar.
 * @author Tongfei Chen
 * @since 0.5.0
 */
class ProgressState {

    String task;

    int current = 0;
    int max = 0;

    LocalDateTime startTime = null;

    String extraMessage = "";

    ProgressState(String task, int initialMax) {
        this.task = task;
        this.max = initialMax;
    }

    synchronized void maxHint(int n) {
        max = n;
    }

    synchronized void stepBy(int n) {
        current += n;
        if (current > max) max = current;
    }

    synchronized void stepTo(int n) {
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

    synchronized int getCurrent() {
        return current;
    }

    synchronized int getMax() {
        return max;
    }

}
