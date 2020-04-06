package me.tongfei.progressbar;

/**
 * @author Tongfei Chen
 * @since 0.5.0
 */
class ProgressThread implements Runnable {

    private ProgressState progress;
    private ProgressBarRenderer renderer;
    long updateInterval;
    private ProgressBarConsumer consumer;

    ProgressThread(
            ProgressState progress,
            ProgressBarRenderer renderer,
            long updateInterval,
            ProgressBarConsumer consumer
    ) {
        this.progress = progress;
        this.renderer = renderer;
        this.updateInterval = updateInterval;
        this.consumer = consumer;
    }

    private void refresh() {
        String rendered = renderer.render(progress, consumer.getMaxProgressLength());
        consumer.accept(rendered);
    }

    void closeConsumer() {
        consumer.close();
        // force refreshing after being "interrupted"
        refresh();
    }

    public void run() {
        refresh();
    }

}
