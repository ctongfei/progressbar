package me.tongfei.progressbar;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Builder class for {@link ProgressBar}s.
 * @author Tongfei Chen
 * @since 0.7.0
 */
public class ProgressBarBuilder {

    private String task = "";
    private long initialMax = -1;
    private int updateIntervalMillis = 1000;
    private ProgressBarStyle style = ProgressBarStyle.COLORFUL_UNICODE_BLOCK;
    private ProgressBarConsumer consumer = null;
    private String unitName = "";
    private long unitSize = 1;
    private boolean showSpeed = false;
    private DecimalFormat speedFormat;
    private ChronoUnit speedUnit = ChronoUnit.SECONDS;
    private long processed = 0;
    private Duration elapsed = Duration.ZERO;

    public ProgressBarBuilder() { }

    public ProgressBarBuilder setTaskName(String task) {
        this.task = task;
        return this;
    }

    public ProgressBarBuilder setInitialMax(long initialMax) {
        this.initialMax = initialMax;
        return this;
    }

    public ProgressBarBuilder setStyle(ProgressBarStyle style) {
        this.style = style;
        return this;
    }

    public ProgressBarBuilder setUpdateIntervalMillis(int updateIntervalMillis) {
        this.updateIntervalMillis = updateIntervalMillis;
        return this;
    }

    public ProgressBarBuilder setConsumer(ProgressBarConsumer consumer) {
        this.consumer = consumer;
        return this;
    }

    public ProgressBarBuilder setUnit(String unitName, long unitSize) {
        this.unitName = unitName;
        this.unitSize = unitSize;
        return this;
    }

    public ProgressBarBuilder showSpeed() {
        return showSpeed(new DecimalFormat("#.0"));
    }

    public ProgressBarBuilder showSpeed(DecimalFormat speedFormat) {
        this.showSpeed = true;
        this.speedFormat = speedFormat;
        return this;
    }

    public ProgressBarBuilder setSpeedUnit(ChronoUnit speedUnit) {
        this.speedUnit = speedUnit;
        return this;
    }

    /**
     * Sets elapsedBeforeStart duration and number of processed units.
     * @param processed amount of processed units
     * @param elapsed duration of
     */
    public ProgressBarBuilder startsFrom(long processed, Duration elapsed) {
        this.processed = processed;
        this.elapsed = elapsed;
        return this;
    }

    public ProgressBar build() {
        return new ProgressBar(
                task,
                initialMax,
                updateIntervalMillis,
                processed,
                elapsed,
                new DefaultProgressBarRenderer(style, unitName, unitSize, showSpeed, speedFormat,speedUnit),
                consumer == null ? Util.createConsoleConsumer() : consumer
        );
    }
}
