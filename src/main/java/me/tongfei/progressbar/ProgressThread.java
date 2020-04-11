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
    private boolean active = true;

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

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void run() {
        if (!active) {
            refresh();
            consumer.close();
            return;
        }

        refresh();
    }

}
