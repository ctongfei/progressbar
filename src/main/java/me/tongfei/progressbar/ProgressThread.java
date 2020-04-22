package me.tongfei.progressbar;

/**
 * @author Tongfei Chen
 * @since 0.5.0
 */
class ProgressThread implements Runnable {

    ProgressState progress;
    private long updateInterval;
    private ProgressBarRenderer renderer;
    private ProgressBarConsumer consumer;
    private boolean active = true;

    private boolean paused;

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

    void pause() {
        paused = true;
    }

    void resume() {
        paused = false;
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
        try {
            while (!Thread.interrupted()) {
                if (!paused)
                    refresh();
                Thread.sleep(updateInterval);
            }
        } catch (InterruptedException ignored) {
            refresh();
            consumer.close();
            TerminalUtils.closeTerminal();
            return;
        }

        refresh();
    }

}
