package me.tongfei.progressbar;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

class ProgressStateImmutable {
    final String task;
    final boolean indefinite;
    final long current;
    final long max;
    final Instant startTime;
    final String extraMessage;
    final List<ProgressStateImmutable> children;
    final Duration elapsed;

    public ProgressStateImmutable(String task, boolean indefinite, long current, long max, Instant startTime, String extraMessage, Duration elapsed, List<ProgressStateImmutable> children) {
        this.task = task;
        this.indefinite = indefinite;
        this.current = current;
        this.max = max;
        this.startTime = startTime;
        this.extraMessage = extraMessage;
        this.children = children;
        this.elapsed = elapsed;
    }

    int progress() {
        if (!children.isEmpty()) {
            return (int) children.stream().mapToInt(ProgressStateImmutable::progress).average().orElse(0);
        }
        return (int) Math.floor(100.0 * current / max);
    }

    // The progress, normalized to range [0, 1].
    double getNormalizedProgress() {
        if (!children.isEmpty())
            return children.stream().mapToDouble(ProgressStateImmutable::getNormalizedProgress).average().orElse(0);
        if (max <= 0) return 0.0;
        else return ((double) current) / max;
    }

    Duration eta() {
        if (!children.isEmpty())
            return children.stream().map(ProgressStateImmutable::eta).max(Duration::compareTo).orElse(Duration.ZERO);
        return elapsed.dividedBy(current).multipliedBy(max - current);
    }

    double speed() {
        if (!children.isEmpty())
            return children.stream().mapToDouble(ProgressStateImmutable::speed).average().orElse(0);
        return (double) current / elapsed.getSeconds();
    }

    boolean isDone() {
        return current == max;
    }

    public String getTask() {
        return task;
    }

    public boolean isIndefinite() {
        return indefinite;
    }

    public long getCurrent() {
        return current;
    }

    public long getMax() {
        return max;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public String getExtraMessage() {
        return extraMessage;
    }

    public List<ProgressStateImmutable> getChildren() {
        return children;
    }
}