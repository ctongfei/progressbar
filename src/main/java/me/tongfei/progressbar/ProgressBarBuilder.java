package me.tongfei.progressbar;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Function;

/**
 * Builder class for {@link ProgressBar}s.
 * @author Tongfei Chen
 * @since 0.7.0
 */
public class ProgressBarBuilder {

    private String task = "";
    private Integer taskFixedLength = null;
    private long initialMax = -1;
    private int updateIntervalMillis = 1000;
    private boolean continuousUpdate = false;
    private ProgressBarStyle style = ProgressBarStyle.COLORFUL_UNICODE_BLOCK;
    private ProgressBarConsumer consumer = null;
    private boolean clearDisplayOnFinish = false;
    private String unitName = "";
    private long unitSize = 1;
    private TimeFormat timeFormat = TimeFormat.DEFAULT;
    private boolean showSpeed = false;
    private boolean hideEta = false;
    private Function<ProgressState, Optional<Duration>> eta = Util::linearEta;
    private DecimalFormat speedFormat;
    private ChronoUnit speedUnit = ChronoUnit.SECONDS;
    private long processed = 0;
    private Duration elapsed = Duration.ZERO;
    private int maxRenderedLength = -1;

    private ProgressBarRenderer renderer = null;

    public ProgressBarBuilder() { }

    public ProgressBarBuilder setTaskName(String task) {
        this.task = task;
        return this;
    }

    public ProgressBarBuilder setTaskFixedLength(Integer fixedLength) {
        this.taskFixedLength = fixedLength;
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

    public ProgressBarBuilder setRenderer(ProgressBarRenderer renderer) {
        this.renderer = renderer;
        return this;
    }

    public ProgressBarBuilder setTimeFormat(TimeFormat timeFormat) {
        this.timeFormat = timeFormat;
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

    public ProgressBarBuilder hideEta() {
        this.hideEta = true;
        return this;
    }

    public ProgressBarBuilder setEtaFunction(Function<ProgressState, Optional<Duration>> eta) {
        this.hideEta = false;
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
                taskFixedLength,
                initialMax,
                updateIntervalMillis,
                continuousUpdate,
                clearDisplayOnFinish,
                processed,
                elapsed,
                (renderer == null
                        ? new DefaultProgressBarRenderer(
                        style, unitName, unitSize,
                        showSpeed, speedFormat, speedUnit,
                        !hideEta, eta, timeFormat)
                        : renderer
                ),
                (consumer == null
                        ? Util.createConsoleConsumer(maxRenderedLength)
                        : consumer
                )
        );
    }
}
