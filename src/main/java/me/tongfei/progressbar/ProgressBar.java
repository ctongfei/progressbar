package me.tongfei.progressbar;

import java.io.OutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.time.LocalDateTime;
import jline.TerminalFactory;

/**
 * A simple console-based progress bar.
 * @author Tongfei Chen (ctongfei@gmail.com).
 */
public class ProgressBar {

    private static String symbols = " ▏▎▍▌▋▊▉█";

    private String task;
    private final int consoleRightMargin = 4;
    private int length = 0;
    private int current = 0;
    private int updateIntervalMillis = 1000;
    private int max;
    private LocalDateTime startTime = null;
    private LocalDateTime lastTime = null;
    private String extraMessage = "";
    private PrintStream consoleStream = System.err;
    final private Object syncRoot = new Object();

    /**
     * Creates a progress bar with the specific task name and initial maximum value.
     * @param task Task name
     * @param initialMax Initial maximum value
     */
    public ProgressBar(String task, int initialMax) {
        this.task = task;
        this.max = initialMax;
    }

    /**
     * Creates a progress bar with the specific task name, initial maximum value
     * and customized update interval (default 1000 ms).
     * @param task Task name
     * @param initialMax Initial maximum value
     * @param updateIntervalMillis Update interval (default value 1000 ms)
     */
    public ProgressBar(String task, int initialMax, int updateIntervalMillis) {
        this.task = task;
        this.max = initialMax;
        this.updateIntervalMillis = updateIntervalMillis;
    }


    /**
     * Creates a progress bar with the specific task name, initial maximum value,
     * customized update interval (default 1000 ms) and the PrintStream to be used.
     * @param task Task name
     * @param initialMax Initial maximum value
     * @param updateIntervalMillis Update interval (default value 1000 ms)
     * @param os Print stream (default value System.err)
     */
    public ProgressBar(String task, int initialMax, int updateIntervalMillis, PrintStream os) {
        this.task = task;
        this.max = initialMax;
        this.updateIntervalMillis = updateIntervalMillis;
        this.consoleStream = os;
    }

    private String repeat(char x, int n) {
        if (n <= 0) return "";
        char[] s = new char[n];
        for (int i = 0; i < n; i++)
            s[i] = x;
        return new String(s);
    }

    private int progress() {
        if (max == 0) return 0;
        else return ((int)Math.round(((double)current) / max * length));
    }

    private int progressIntegralPart() {
        if (max == 0) return 0;
        else return ((int)Math.floor(((double)current) / max * length));
    }

    private int progressFractionalPart() {
        if (max == 0) return 0;
        else {
            double x = (double)current / max * length;
            return Math.round((int)((x - Math.floor(x)) * 8));
        }
    }


    private String formatDuration(Duration d) {
        long s = d.getSeconds();
        return String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60);
    }

    private String eta(Duration elapsed) {
        if (max == 0) return "?";
        else if (current == 0) return "?";
        else return formatDuration(elapsed.dividedBy(current).multipliedBy(max - current));
    }

    private String percentage() {
        String res;
        if (max == 0) res = "? %";
        else res = String.valueOf(Math.round(((double)current) / max * 100)) + "%";
        return repeat(' ', 4 - res.length()) + res;
    }

    private String ratio() {
        int l = String.valueOf(max).length();
        String c = String.valueOf(current);
        return repeat(' ', l - c.length()) + c + "/" + String.valueOf(max);
    }

    private int consoleWidth() {
        return TerminalFactory.get().getWidth();
    }

    private void forceShow(LocalDateTime currentTime) {
        consoleStream.print('\r');
        Duration elapsed = Duration.between(startTime, currentTime);
        lastTime = currentTime;

        String prefix = task + " " + percentage() + " │";
        String suffix = "│ " + ratio() + " (" + formatDuration(elapsed) + " / " + eta(elapsed) + ") " + extraMessage;

        length = consoleWidth() - consoleRightMargin - prefix.length() - suffix.length();

        String message = prefix + repeat('█', progressIntegralPart()) + symbols.charAt(progressFractionalPart()) + repeat(' ', length - progressIntegralPart() - 1) + suffix;
        int lastMessageLength = message.length();
        consoleStream.print(message + repeat(' ', lastMessageLength - message.length()));
    }

    private void show() {
        LocalDateTime currentTime = LocalDateTime.now();
        if (Duration.between(lastTime, currentTime).toMillis() >= updateIntervalMillis)
            forceShow(currentTime);
    }

    /**
     * Starts this progress bar.
     */
    public void start() {
        startTime = LocalDateTime.now();
        lastTime = LocalDateTime.now();
        forceShow(lastTime);
    }

    /**
     * Advances this progress bar by a specific amount.
     * @param n Step size
     */
    public void stepBy(int n) {
        synchronized (syncRoot) {
            current += n;
            if (current > max)
                max = current;
        }
        show();
    }

    /**
     * Advances this progress bar to the specific progress value.
     * @param n New progress value
     */
    public void stepTo(int n) {
        synchronized (syncRoot) {
            current = n;
            if (current > max)
                max = current;
        }
        show();
    }

    /**
     * Advances this progress bar by one step.
     */
    public void step() {
        stepBy(1);
    }

    /**
     * Gives a hint to the maximum value of the progress bar.
     * @param n Hint of the maximum value
     */
    public void maxHint(int n) {
        max = n;
        show();
    }

    /**
     * Stops this progress bar.
     */
    public void stop() {
        forceShow(LocalDateTime.now());
        consoleStream.println();
        startTime = null;
    }

    /**
     * Sets the extra message at the end of the progress bar.
     * @param newExtraMessage New message
     */
    public void setExtraMessage(String newExtraMessage) {
        length = length + extraMessage.length() - newExtraMessage.length();
        extraMessage = newExtraMessage;
    }

}
