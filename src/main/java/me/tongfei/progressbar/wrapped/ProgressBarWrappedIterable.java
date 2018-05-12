package me.tongfei.progressbar.wrapped;

import me.tongfei.progressbar.ProgressBarBuilder;

import java.util.Iterator;

/**
 * @author Tongfei Chen
 * @since 0.6.0
 */
public class ProgressBarWrappedIterable<T> implements Iterable<T> {

    Iterable<T> underlying;
    ProgressBarBuilder pbb;

    public ProgressBarWrappedIterable(Iterable<T> underlying, ProgressBarBuilder pbb) {
        this.underlying = underlying;
        this.pbb = pbb;
    }

    @Override
    public Iterator<T> iterator() {
        return new ProgressBarWrappedIterator<>(
                underlying.iterator(),
                pbb
                        .setInitialMax(underlying.spliterator().getExactSizeIfKnown())
                        // if size unknown, -1, hence indefinite progress bar
                        .build().start()
        );
    }
}
