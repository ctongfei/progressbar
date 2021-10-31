package me.tongfei.progressbar;

/**
 * Represents the action that is executed for each refresh of a progress bar.
 */
class ProgressUpdateAction implements Runnable {

    ProgressState progress;
    private ProgressBarRenderer renderer;
    private ProgressBarConsumer consumer;
    private long last;
    private boolean forceRefresh = false;

    ProgressUpdateAction(
            ProgressState progress,
            ProgressBarRenderer renderer,
            ProgressBarConsumer consumer
    ) {
        this.progress = progress;
        this.renderer = renderer;
        this.consumer = consumer;
        this.last = progress.start;
    }

    private void refresh() {
        if (forceRefresh || progress.current > last) {
            String rendered = renderer.render(progress, consumer.getMaxRenderedLength());
            consumer.accept(rendered);
            last = progress.current;
        }
        // else do nothing: only print when actual progress is made (#91).
    }

    public void setForceRefresh() {
        forceRefresh = true;
    }
    
    public void run() {
        if (!progress.paused) refresh();
        if (!progress.alive) {
            consumer.close();
            TerminalUtils.closeTerminal();
        }
    }

}
