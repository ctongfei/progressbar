package me.tongfei.progressbar;

import java.text.DecimalFormat;
import java.time.temporal.ChronoUnit;

/**
 * Builder class for {@link ProgressBar}s.
 * @author Tongfei Chen
 * @since 0.7.0
 */
public class ProgressBarBuilder {

    private String task = "";
    private long initialMax = 0;
    private int updateIntervalMillis = 1000;
    private ProgressBarStyle style = ProgressBarStyle.COLORFUL_UNICODE_BLOCK;
    private ProgressBarConsumer consumer = null;
    private String unitName = "";
    private long unitSize = 1;
    private boolean showSpeed = false;
    private DecimalFormat speedFormat;
    private ChronoUnit speedUnit = ChronoUnit.SECONDS;
    private long startFrom = 0;
    private long elapsedSecond = 0;

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
    /**
     * Speed Unit of eta.
     *
     * @param speedUnit supported only second, minutes, hours and days, default
     * is second.
     * @return
     */
    public ProgressBarBuilder setSpeedUnit(ChronoUnit speedUnit) {
        this.speedUnit = speedUnit;
        return this;
    }
    /**
     * @param startFrom the startFrom to set
     * @return 
     */
    public ProgressBarBuilder setStartFrom(long startFrom) {
        this.startFrom = startFrom;
        return this;
    }

    /**
     * you can set parameters of elapsed duration and number of processed units
     * if you want to continue a process before started
     *
     * @param startFrom amount of processed units
     * @param elapsedSecond you can convert from other formats
     * @return
     */
    public ProgressBarBuilder setBeforeProcessed(long startFrom, long elapsedSecond) {
        this.startFrom = startFrom;
        this.elapsedSecond = elapsedSecond;
        return this;
    }

    public ProgressBar build() {
        if (consumer == null)
            consumer = new ConsoleProgressBarConsumer();

        return new ProgressBar(
                task,
                initialMax,
                updateIntervalMillis,
                new DefaultProgressBarRenderer(style, unitName, unitSize, showSpeed, speedFormat,speedUnit),
                consumer,startFrom, elapsedSecond
        );
    }
}
