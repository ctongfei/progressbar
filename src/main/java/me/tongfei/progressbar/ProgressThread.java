package me.tongfei.progressbar;

import java.io.PrintStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * @author Tongfei Chen
 * @since 0.5.0
 */
class ProgressThread implements Runnable {

    volatile boolean killed;
    ProgressBarStyle style;
    ProgressState progress;
    long updateInterval;
    PrintStream consoleStream;
    Terminal terminal;
    int consoleWidth = 80;

    static int consoleRightMargin = 2;

    int length;

    ProgressThread(ProgressState progress, ProgressBarStyle style, long updateInterval, PrintStream consoleStream) {
        this.progress = progress;
        this.style = style;
        this.updateInterval = updateInterval;
        this.consoleStream = consoleStream;
        this.killed = false;

        try {
            this.terminal = TerminalBuilder.terminal();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (terminal.getWidth() >= 10)  // Workaround for issue #23 under IntelliJ
            consoleWidth = terminal.getWidth();
    }

    // between 0 and 1
    double progress() {
        if (progress.max <= 0) return 0.0;
        else return ((double)progress.current) / progress.max;
    }

    // Number of full blocks
    int progressIntegralPart() {
        return (int)(progress() * length);
    }

    int progressFractionalPart() {
        double p = progress() * length;
        double fraction = (p - Math.floor(p)) * style.fractionSymbols.length();
        return (int) Math.floor(fraction);
    }

    String eta(Duration elapsed) {
        if (progress.max <= 0 || progress.indefinite) return "?";
        else if (progress.current == 0) return "?";
        else return Util.formatDuration(
                elapsed.dividedBy(progress.current)
                        .multipliedBy(progress.max - progress.current));
    }

    String percentage() {
        String res;
        if (progress.max <= 0 || progress.indefinite) res = "? %";
        else res = String.valueOf((int) Math.floor(100.0 * progress.current / progress.max)) + "%";
        return Util.repeat(' ', 4 - res.length()) + res;
    }

    String ratio() {
        String m = progress.indefinite ? "?" : String.valueOf(progress.max);
        String c = String.valueOf(progress.current);
        return Util.repeat(' ', m.length() - c.length()) + c + "/" + m;
    }

    void refresh() {
        consoleStream.print('\r');

        LocalDateTime currTime = LocalDateTime.now();
        Duration elapsed = Duration.between(progress.startTime, currTime);

        String prefix = progress.task + " " + percentage() + " " + style.leftBracket;

        int maxSuffixLength = Math.max(0, consoleWidth - consoleRightMargin - prefix.length() - 10);
        String suffix = style.rightBracket + " " + ratio() + " (" + Util.formatDuration(elapsed) + " / " + eta(elapsed) + ") " + progress.extraMessage;
        if (suffix.length() > maxSuffixLength) suffix = suffix.substring(0, maxSuffixLength);

        length = consoleWidth - consoleRightMargin - prefix.length() - suffix.length();

        StringBuilder sb = new StringBuilder();
        sb.append(prefix);

        // case of indefinite progress bars
        if (progress.indefinite) {
            int pos = (int)(progress.current % length);
            sb.append(Util.repeat(style.space, pos));
            sb.append(style.block);
            sb.append(Util.repeat(style.space, length - pos - 1));
        }
        // case of definite progress bars
        else {
            sb.append(Util.repeat(style.block, progressIntegralPart()));
            if (progress.current < progress.max) {
                sb.append(style.fractionSymbols.charAt(progressFractionalPart()));
                sb.append(Util.repeat(style.space, length - progressIntegralPart() - 1));
            }
        }
        sb.append(suffix);
        String line = sb.toString();

        consoleStream.print(line);
    }

    void kill() {
    	killed = true;
    }

    public void run() {
        try {
            while (!killed) {
                refresh();
                Thread.sleep(updateInterval);
            }
            refresh();
            // do-while loop not right: must force to refresh after stopped
        } catch (InterruptedException ex) { }
    }
}