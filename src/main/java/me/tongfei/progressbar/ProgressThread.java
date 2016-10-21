package me.tongfei.progressbar;

import jline.TerminalFactory;
import java.io.PrintStream;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Tongfei Chen
 * @since 0.5.0
 */
public class ProgressThread implements Runnable {

    volatile boolean running;
    Progress progress;
    long updateInterval;
    PrintStream consoleStream;

    static int consoleRightMargin = 3;

    int length;

    ProgressThread(Progress progress, long updateInterval, PrintStream consoleStream) {
        this.progress = progress;
        this.updateInterval = updateInterval;
        this.consoleStream = consoleStream;
    }

    // between 0 and 1
    double progress() {
        if (progress.max == 0) return 0.0;
        else return ((double)progress.current) / progress.max;
    }

    // Number of full blocks
    int progressIntegralPart() {
        return (int)(progress() * length);
    }

    int progressFractionalPart() {
        double p = progress() * length;
        double fraction = (p - Math.floor(p)) * 8;
        return (int) Math.floor(fraction);
    }

    String eta(Duration elapsed) {
        if (progress.max == 0) return "?";
        else if (progress.current == 0) return "?";
        else return Util.formatDuration(
                elapsed.dividedBy(progress.current)
                        .multipliedBy(progress.max - progress.current));
    }

    String percentage() {
        String res;
        if (progress.max == 0) res = "? %";
        else res = String.valueOf(Math.round(100.0 * progress.current / progress.max)) + "%";
        return Util.repeat(' ', 4 - res.length()) + res;
    }

    String ratio() {
        String m = String.valueOf(progress.max);
        String c = String.valueOf(progress.current);
        return Util.repeat(' ', m.length() - c.length()) + c + "/" + m;
    }

    int consoleWidth() {
        return TerminalFactory.get().getWidth();
    }

    void refresh() {
        consoleStream.print('\r');
        LocalDateTime currTime = LocalDateTime.now();
        Duration elapsed = Duration.between(progress.startTime, currTime);

        String prefix = progress.task + " " + percentage() + " │";
        String suffix = "│ " + ratio() + " (" + Util.formatDuration(elapsed) + " / " + eta(elapsed) + ") " + progress.extraMessage;

        length = consoleWidth() - consoleRightMargin - prefix.length() - suffix.length();

        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(Util.repeat(Util.block, progressIntegralPart()));
        if (progress.current < progress.max) {
            sb.append(Util.symbols.charAt(progressFractionalPart()));
            sb.append(Util.repeat(' ', length - progressIntegralPart() - 1));
        }
        sb.append(suffix);
        String line = sb.toString();

        consoleStream.print(line);
    }

    void kill() {
        running = false;
    }

    public void run() {
        running = true;
        try {
            while (running) {
                refresh();
                Thread.sleep(updateInterval);
            }
            refresh();
            consoleStream.print("\n");
        } catch (InterruptedException ex) { }
    }
}

