package me.tongfei.progressbar;

/**
 * Represents the action that is executed for each refresh of a progress bar.
 */
class ProgressUpdateAction implements Runnable {

    ProgressState progress;
    private ProgressBarRenderer renderer;
    private ProgressBarConsumer consumer;
    private boolean continuousUpdate;
    private boolean clearDisplayOnFinish;
    volatile private long last;
    volatile private boolean first;

    ProgressUpdateAction(
            ProgressState progress,
            ProgressBarRenderer renderer,
            ProgressBarConsumer consumer,
            boolean continuousUpdate,
            boolean clearDisplayOnFinish
    ) {
        this.progress = progress;
        this.renderer = renderer;
        this.consumer = consumer;
        this.continuousUpdate = continuousUpdate;
        this.clearDisplayOnFinish = clearDisplayOnFinish;
        this.last = progress.start;
        this.first = true;
    }

    void refresh() {
        if (continuousUpdate || (progress.current > last))
            forceRefresh();
        // else do nothing: only print when actual progress is made (#91).
    }

    public void forceRefresh() {
        String rendered = renderer.render(progress, consumer.getMaxRenderedLength());
        consumer.accept(rendered);
        last = progress.current;
    }

    public void run() {
        if (first) {
            forceRefresh();
            first = false;
        }
        else {
            if (!progress.paused) refresh();
            if (!progress.alive) {
                forceRefresh();
                if (clearDisplayOnFinish) consumer.clear();
                consumer.close();
                TerminalUtils.closeTerminal();
            }
        }
    }

}
