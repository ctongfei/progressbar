package me.tongfei.progressbar;

import me.tongfei.progressbar.wrapped.ProgressBarWrappedInputStream;
import me.tongfei.progressbar.wrapped.ProgressBarWrappedIterable;
import me.tongfei.progressbar.wrapped.ProgressBarWrappedIterator;
import me.tongfei.progressbar.wrapped.ProgressBarWrappedSpliterator;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.BaseStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static me.tongfei.progressbar.Util.createConsoleConsumer;

/**
 * A console-based progress bar with minimal runtime overhead.
 * @author Tongfei Chen
 */
public class ProgressBar implements AutoCloseable {

    private ProgressState progress;
    private ProgressUpdateAction action;
    private ScheduledFuture<Void> scheduledTask;

    /**
     * Creates a progress bar with the specific taskName name and initial maximum value.
     * @param task Task name
     * @param initialMax Initial maximum value
     */
    public ProgressBar(String task, long initialMax) {
        this(task, initialMax, 1000, System.err, ProgressBarStyle.COLORFUL_UNICODE_BLOCK, "", 1, false, null, ChronoUnit.SECONDS, 0L, Duration.ZERO);
    }

    /**
     * Creates a progress bar with the specific taskName name, initial maximum value,
     * customized update interval (default 1000 ms), the PrintStream to be used, and output style.
     * @param task Task name
     * @param initialMax Initial maximum value
     * @param updateIntervalMillis Update interval (default value 1000 ms)
     * @param style Output style (default value ProgressBarStyle.UNICODE_BLOCK)
     * @param showSpeed Should the calculated speed be displayed
     * @param speedFormat Speed number format
     */
    public ProgressBar(
            String task,
            long initialMax,
            int updateIntervalMillis,
            PrintStream os,
            ProgressBarStyle style,
            String unitName,
            long unitSize,
            boolean showSpeed,
            DecimalFormat speedFormat,
            ChronoUnit speedUnit,
            long processed,
            Duration elapsed
    ) {
        this(task, initialMax, updateIntervalMillis, processed, elapsed,
                new DefaultProgressBarRenderer(style, unitName, unitSize, showSpeed, speedFormat, speedUnit),
                createConsoleConsumer(os)
        );
    }

    /**
     * Creates a progress bar with the specific name, initial maximum value, customized update interval (default 1s),
     * and the provided progress bar renderer ({@link ProgressBarRenderer}) and consumer ({@link ProgressBarConsumer}).
     * @param task Task name
     * @param initialMax Initial maximum value
     * @param updateIntervalMillis Update time interval (default value 1000ms)
     * @param processed Initial completed process value
     * @param elapsed Initial elapsedBeforeStart second before
     * @param renderer Progress bar renderer
     * @param consumer Progress bar consumer
     */
    public ProgressBar(
            String task,
            long initialMax,
            int updateIntervalMillis,
            long processed,
            Duration elapsed,
            ProgressBarRenderer renderer,
            ProgressBarConsumer consumer
    ) {
        this.progress = new ProgressState(task, initialMax, processed, elapsed);
        this.action = new ProgressUpdateAction(progress, renderer, consumer);
        scheduledTask = (ScheduledFuture<Void>) Util.executor.scheduleAtFixedRate(
                action, 0, updateIntervalMillis, TimeUnit.MILLISECONDS
        );
    }

    /**
     * Advances this progress bar by a specific amount.
     * @param n Step size
     */
    public ProgressBar stepBy(long n) {
        progress.stepBy(n);
        return this;
    }

    /**
     * Advances this progress bar to the specific progress value.
     * @param n New progress value
     */
    public ProgressBar stepTo(long n) {
        progress.stepTo(n);
        return this;
    }

    /**
     * Advances this progress bar by one step.
     */
    public ProgressBar step() {
        progress.stepBy(1);
        return this;
    }

    /**
     * Gives a hint to the maximum value of the progress bar.
     * @param n Hint of the maximum value
     */
    public ProgressBar maxHint(long n) {
        if (n < 0)
            progress.setAsIndefinite();
        else {
            progress.setAsDefinite();
            progress.maxHint(n);
        }
        return this;
    }

    /**
     * Pauses this current progress.
     */
    public ProgressBar pause() {
        progress.pause();
        return this;
    }

    /**
     * Resumes this current progress.
     */
    public ProgressBar resume() {
        progress.resume();
        return this;
    }

    /**
     * <p>Stops this progress bar, effectively stops tracking the underlying process.</p>
     * <p>Implements the {@link AutoCloseable} interface which enables the try-with-resource
     * pattern with progress bars.</p>
     * @since 0.7.0
     */
    @Override
    public void close() {
        scheduledTask.cancel(false);
        progress.kill();
        try {
            Util.executor.schedule(action, 0, TimeUnit.NANOSECONDS).get();
        } catch (InterruptedException | ExecutionException e) { /* NOOP */ }
    }

    /**
     * Sets the extra message at the end of the progress bar.
     * @param msg New message
     */
    public ProgressBar setExtraMessage(String msg) {
        progress.setExtraMessage(msg);
        return this;
    }

	/**
     * Returns the current progress.
     */
    public long getCurrent() {
        return progress.getCurrent();
    }

    /**
     * Returns the maximum value of this progress bar.
     */
    public long getMax() {
        return progress.getMax();
    }

    /**
     * Returns the name of this task.
     */
    public String getTaskName() {
        return progress.getTaskName();
    }

    /**
     * Returns the extra message at the end of the progress bar.
     */
    public String getExtraMessage() {
        return progress.getExtraMessage();
    }

    // STATIC WRAPPER METHODS

    /**
     * Wraps an iterator so that when iterated, a progress bar is shown to track the traversal progress.
     * @param it Underlying iterator
     * @param task Task name
     */
    public static <T> Iterator<T> wrap(Iterator<T> it, String task) {
        return wrap(it,
                new ProgressBarBuilder().setTaskName(task).setInitialMax(-1)
        ); // indefinite progress bar
    }

    /**
     * Wraps an iterator so that when iterated, a progress bar is shown to track the traversal progress.
     * @param it Underlying iterator
     * @param pbb Progress bar builder
     */
    public static <T> Iterator<T> wrap(Iterator<T> it, ProgressBarBuilder pbb) {
        return new ProgressBarWrappedIterator<>(it, pbb.build());
    }

    /**
     * Wraps an {@link Iterable} so that when iterated, a progress bar is shown to track the traversal progress.
     * <p>
     * Sample usage: {@code
     *   for (T x : ProgressBar.wrap(collection, "Traversal")) { ... }
     * }
     * </p>
     * @param ts Underlying iterable
     * @param task Task name
     */
    public static <T> Iterable<T> wrap(Iterable<T> ts, String task) {
        return wrap(ts, new ProgressBarBuilder().setTaskName(task));
    }

    /**
     * Wraps an {@link Iterable} so that when iterated, a progress bar is shown to track the traversal progress.
     * For this function the progress bar can be fully customized by using a {@link ProgressBarBuilder}.
     * @param ts Underlying iterable
     * @param pbb An instance of a {@link ProgressBarBuilder}
     */
    public static <T> Iterable<T> wrap(Iterable<T> ts, ProgressBarBuilder pbb) {
        long size = ts.spliterator().getExactSizeIfKnown();
        if (size != -1)
            pbb.setInitialMax(size);
        return new ProgressBarWrappedIterable<>(ts, pbb);
    }

    /**
     * Wraps an {@link InputStream} so that when read, a progress bar is shown to track the reading progress.
     * @param is Input stream to be wrapped
     * @param task Name of the progress
     */
    public static InputStream wrap(InputStream is, String task) {
        ProgressBarBuilder pbb = new ProgressBarBuilder().setTaskName(task).setInitialMax(Util.getInputStreamSize(is));
        return wrap(is, pbb);
    }

    /**
     * Wraps an {@link InputStream} so that when read, a progress bar is shown to track the reading progress.
     * For this function the progress bar can be fully customized by using a {@link ProgressBarBuilder}.
     * @param is Input stream to be wrapped
     * @param pbb An instance of a {@link ProgressBarBuilder}
     */
    public static InputStream wrap(InputStream is, ProgressBarBuilder pbb) {
        long size = Util.getInputStreamSize(is);
        if (size != -1)
            pbb.setInitialMax(size);
        return new ProgressBarWrappedInputStream(is, pbb.build());
    }

    /**
     * Wraps a {@link Spliterator} so that when iterated, a progress bar is shown to track the traversal progress.
     * @param sp Underlying spliterator
     * @param task Task name
     */
    public static <T> Spliterator<T> wrap(Spliterator<T> sp, String task) {
        ProgressBarBuilder pbb = new ProgressBarBuilder().setTaskName(task);
        return wrap(sp, pbb);
    }

    /**
     * Wraps a {@link Spliterator} so that when iterated, a progress bar is shown to track the traversal progress.
     * For this function the progress bar can be fully customized by using a {@link ProgressBarBuilder}.
     * @param sp Underlying spliterator
     * @param pbb An instance of a {@link ProgressBarBuilder}
     */
    public static <T> Spliterator<T> wrap(Spliterator<T> sp, ProgressBarBuilder pbb) {
        long size = sp.getExactSizeIfKnown();
        if (size != -1)
            pbb.setInitialMax(size);
        return new ProgressBarWrappedSpliterator<>(sp, pbb.build());
    }

    /**
     * Wraps a {@link Stream} so that when iterated, a progress bar is shown to track the traversal progress.
     * @param stream Underlying stream (can be sequential or parallel)
     * @param task Task name
     */
    public static <T, S extends BaseStream<T, S>> Stream<T> wrap(S stream, String task) {
        ProgressBarBuilder pbb = new ProgressBarBuilder().setTaskName(task);
        return wrap(stream, pbb);
    }

    /**
     * Wraps a {@link Stream} so that when iterated, a progress bar is shown to track the traversal progress.
     * For this function the progress bar can be fully customized by using a {@link ProgressBarBuilder}.
     * @param stream Underlying stream (can be sequential or parallel)
     * @param pbb An instance of a {@link ProgressBarBuilder}
     */
    public static <T, S extends BaseStream<T, S>> Stream<T> wrap(S stream, ProgressBarBuilder pbb) {
        Spliterator<T> sp = wrap(stream.spliterator(), pbb);
        return StreamSupport.stream(sp, stream.isParallel());
    }

    /**
     * Wraps an array so that when iterated, a progress bar is shown to track the traversal progress.
     * @param array Array to be wrapped
     * @param task Task name
     * @return Wrapped array, of type {@link Stream}.
     */
    public static <T> Stream<T> wrap(T[] array, String task) {
        ProgressBarBuilder pbb = new ProgressBarBuilder().setTaskName(task).setInitialMax(array.length);
        return wrap(array, pbb);
    }

    /**
     * Wraps an array so that when iterated, a progress bar is shown to track the traversal progress.
     * For this function the progress bar can be fully customized by using a {@link ProgressBarBuilder}.
     * @param array Array to be wrapped
     * @param pbb An instance of a {@link ProgressBarBuilder}
     * @return Wrapped array, of type {@link Stream}.
     */
    public static <T> Stream<T> wrap(T[] array, ProgressBarBuilder pbb) {
        return wrap(Arrays.stream(array), pbb);
    }

}
