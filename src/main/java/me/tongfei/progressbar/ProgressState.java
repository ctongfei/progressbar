package me.tongfei.progressbar;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;


/**
 * Encapsulates the internal states of a progress bar.
 * @author Tongfei Chen
 * @since 0.5.0
 */
class ProgressState {

    private final String task;
    private boolean indefinite = false;
    private long current = 0;
    private long max = 0;
    private Instant startTime = Instant.now();
    private String extraMessage = "";
    private final List<ProgressState> children = new LinkedList<>();
    private Duration elapsed;
    private boolean done = false;

    ProgressState(String task, long initialMax) {
        this.task = task;
        this.max = initialMax;
        if (initialMax < 0) indefinite = true;
    }

    synchronized ProgressStateImmutable getState() {
        List<ProgressStateImmutable> childrenStates = emptyList();
        if (!children.isEmpty()) {
            childrenStates = children.stream().map(ProgressState::getState).collect(Collectors.toList());
            stepTo(childrenStates.stream().filter(ProgressStateImmutable::isDone).count());
        }
        if (!done) {
            elapsed = Duration.between(startTime, Instant.now());
        }
        return new ProgressStateImmutable(task, indefinite, current, max, startTime, extraMessage, elapsed, childrenStates);
    }

    synchronized void addChild(ProgressState child) {
        children.add(child);
        max = children.size();
    }

    synchronized void setIndefinite(boolean indefinite) {
        this.indefinite = indefinite;
    }

    synchronized void setMax(long max) {
        this.max = max;
    }

    synchronized void setExtraMessage(String msg) {
        extraMessage = msg.trim();
    }

    synchronized void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    synchronized void stepBy(long n) {
        stepTo(current + n);
    }

    synchronized void stepTo(long n) {
        current = n;
        if (current > max) max = current;
        if (current == max) {
            elapsed = Duration.between(startTime, Instant.now());
            done = true;
        }
    }
}