package me.tongfei.progressbar.wrapped;

import java.util.Iterator;

/**
 * @author Tongfei Chen
 * @since 0.6.0
 */
public class ProgressBarWrappedIterable<T> implements Iterable<T> {

    Iterable<T> underlying;
    String task;

    public ProgressBarWrappedIterable(Iterable<T> underlying, String task) {
        this.underlying = underlying;
        this.task = task;
    }

    @Override
    public Iterator<T> iterator() {
        return new ProgressBarWrappedIterator<>(
                underlying.iterator(),
                task,
                underlying.spliterator().getExactSizeIfKnown() // if size unknown, -1, hence indefinite progress bar
        );
    }
}
