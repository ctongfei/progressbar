package me.tongfei.progressbar;

import java.io.*;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.Spliterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class Util {

    static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, runnable -> {
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setName("ProgressBar");
        thread.setDaemon(true);
        return thread;
    });

    static ConsoleProgressBarConsumer createConsoleConsumer(int predefinedWidth) {
        PrintStream real = new PrintStream(new FileOutputStream(FileDescriptor.err));
        return createConsoleConsumer(real, predefinedWidth);  // System.err might be overridden by System.setErr
    }

    static ConsoleProgressBarConsumer createConsoleConsumer(PrintStream out) {
        return createConsoleConsumer(out, -1);
    }

    static ConsoleProgressBarConsumer createConsoleConsumer(PrintStream out, int predefinedWidth) {
        return TerminalUtils.hasCursorMovementSupport()
                ? new InteractiveConsoleProgressBarConsumer(out, predefinedWidth)
                : new ConsoleProgressBarConsumer(out, predefinedWidth);
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

    static Optional<Duration> linearETA(ProgressState progress, Duration elapsed) {
        if (progress.max <= 0 || progress.indefinite) return Optional.empty();
        else if (progress.current - progress.start == 0) return Optional.empty();
        else return Optional.of(
                elapsed.dividedBy(progress.current - progress.start).multipliedBy(progress.max - progress.current)
            );
    }

    static long getInputStreamSize(InputStream is) {
        try {
            if (is instanceof FileInputStream)
                return ((FileInputStream) is).getChannel().size();

            // estimate input stream size with InputStream::available
            int available = is.available();
            if (available > 0) return available;
        } catch (IOException ignored) { }
        return -1;
    }

    static <T> long getSpliteratorSize(Spliterator<T> sp) {
        try {
            long size = sp.estimateSize();
            return size != Long.MAX_VALUE ? size : -1;
        } catch (Exception ignored) { }
        return -1;
    }
}
