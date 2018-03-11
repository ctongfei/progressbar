package me.tongfei.progressbar.wrapped;

import me.tongfei.progressbar.ProgressBar;

import java.util.Iterator;

/**
 * @author Tongfei Chen
 * @since 0.6.0
 */
public class ProgressBarWrappedIterator<T> implements Iterator<T> {

    Iterator<T> underlying;
    ProgressBar pb;

    public ProgressBarWrappedIterator(Iterator<T> underlying, String task, long size) {
        this.underlying = underlying;
        pb = new ProgressBar(task, size).start();
    }

    @Override
    public boolean hasNext() {
        boolean r = underlying.hasNext();
        if (!r) pb.stop();
        return r;
    }

    @Override
    public T next() {
        T r = underlying.next();
        pb.step();
        return r;
    }
}
