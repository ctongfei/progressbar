package me.tongfei.progressbar;

import java.text.DecimalFormat;

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

    public ProgressBar build() {
        if (consumer == null)
            consumer = new ConsoleProgressBarConsumer();

        return new ProgressBar(
                task,
                initialMax,
                updateIntervalMillis,
                new DefaultProgressBarRenderer(style, unitName, unitSize, showSpeed, speedFormat),
                consumer
        );
    }
}
