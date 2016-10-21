package me.tongfei.progressbar;

import java.time.LocalDateTime;

/**
 * Encapsulates the internal states of a progress bar.
 * @author Tongfei Chen
 * @since 0.5.0
 */
public class Progress {

    String task;

    int current = 0;
    int max = 0;

    LocalDateTime startTime = null;

    String extraMessage = "";

    Progress(String task, int initialMax) {
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

    synchronized int getCurrent() {
        return current;
    }

}
