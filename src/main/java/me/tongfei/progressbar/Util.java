package me.tongfei.progressbar;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;


class Util {

    static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, runnable -> {
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setName("ProgressBar");
        thread.setDaemon(true);
        return thread;
    });

    static ConsoleProgressBarConsumer createConsoleConsumer() {
        return createConsoleConsumer(System.err);
    }

    static ConsoleProgressBarConsumer createConsoleConsumer(PrintStream out) {
        return TerminalUtils.hasCursorMovementSupport() ?
                new InteractiveConsoleProgressBarConsumer(out) : new ConsoleProgressBarConsumer(out);
    }

    static String repeat(char c, int n) {
        if (n <= 0) return "";
        char[] s = new char[n];
        for (int i = 0; i < n; i++) s[i] = c;
        return new String(s);
    }

    static String formatDuration(Duration d) {
        long s = d.getSeconds();
        return String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60);
    }

    static long getInputStreamSize(InputStream is) {
        try {
            if (is instanceof FileInputStream)
                return ((FileInputStream) is).getChannel().size();
        } catch (IOException e) {
            return -1;
        }
        return -1;
    }
}
