package me.tongfei.progressbar;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Builder class for {@link ProgressBar}s.
 * @author Tongfei Chen
 * @since 0.7.0
 */
public class ProgressBarBuilder {

    private String task = "";
    private long initialMax = -1;
    private int updateIntervalMillis = 1000;
    private boolean continuousUpdate = false;
    private ProgressBarStyle style = ProgressBarStyle.COLORFUL_UNICODE_BLOCK;
    private ProgressBarConsumer consumer = null;
    private boolean clearDisplayOnFinish = false;
    private String unitName = "";
    private long unitSize = 1;
    private boolean showSpeed = false;
    private boolean hideETA = false;
    private BiFunction<ProgressState, Duration, Optional<Duration>> eta = Util::linearETA;
    private DecimalFormat speedFormat;
    private ChronoUnit speedUnit = ChronoUnit.SECONDS;
    private long processed = 0;
    private Duration elapsed = Duration.ZERO;
    private int maxRenderedLength = -1;

    public ProgressBarBuilder() { }

    public ProgressBarBuilder setTaskName(String task) {
        this.task = task;
        return this;
    }

    boolean initialMaxIsSet() {
        return this.initialMax != -1;
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

    public ProgressBarBuilder continuousUpdate() {
        this.continuousUpdate = true;
        return this;
    }

    public ProgressBarBuilder setConsumer(ProgressBarConsumer consumer) {
        this.consumer = consumer;
        return this;
    }

    public ProgressBarBuilder clearDisplayOnFinish() {
        this.clearDisplayOnFinish = true;
        return this;
    }

    public ProgressBarBuilder setUnit(String unitName, long unitSize) {
        this.unitName = unitName;
        this.unitSize = unitSize;
        return this;
    }

    public ProgressBarBuilder setMaxRenderedLength(int maxRenderedLength) {
        this.maxRenderedLength = maxRenderedLength;
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

    public ProgressBarBuilder hideETA() {
        this.hideETA = true;
        return this;
    }

    public ProgressBarBuilder setETAFunction(BiFunction<ProgressState, Duration, Optional<Duration>> eta) {
        this.hideETA = false;
        this.eta = eta;
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
                continuousUpdate,
                clearDisplayOnFinish,
                processed,
                elapsed,
                new DefaultProgressBarRenderer(
                        style, unitName, unitSize,
                        showSpeed, speedFormat, speedUnit,
                        !hideETA, eta),
                consumer == null ? Util.createConsoleConsumer(maxRenderedLength) : consumer
        );
    }
}
