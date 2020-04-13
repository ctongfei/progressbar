package me.tongfei.progressbar;

import java.util.List;

/**
 * @author Tongfei Chen
 * @since 0.5.0
 */
class ProgressThread implements Runnable {

    private ProgressState progress;
    private ProgressBarRenderer renderer;
    private ProgressBarConsumer consumer;
    private boolean active = true;

    ProgressThread(
            ProgressState progress,
            ProgressBarRenderer renderer,
            ProgressBarConsumer consumer
    ) {
        this.progress = progress;
        this.renderer = renderer;
        this.consumer = consumer;
    }

    private void refresh() {
        List<String> rendered = renderer.render(progress, consumer.getMaxProgressLength());
        consumer.accept(rendered);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void run() {
        try {
            if (!active) {
                refresh();
                consumer.close();
                TerminalUtils.closeTerminal();
                return;
            }

            refresh();
        } catch (Exception e) {
            //FIXME: ensure exception in renderer/consumer is not swallowed. What to do really??
            e.printStackTrace();
        }
    }

}
