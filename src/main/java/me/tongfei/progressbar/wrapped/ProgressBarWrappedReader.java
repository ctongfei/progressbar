package me.tongfei.progressbar.wrapped;

import me.tongfei.progressbar.ProgressBar;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * A reader whose progress is tracked by a progress bar.
 * @since 0.9.2
 * @author Tongfei Chen
 */
public class ProgressBarWrappedReader extends FilterReader {

    private ProgressBar pb;
    private long mark = 0;

    public ProgressBarWrappedReader(Reader in, ProgressBar pb) {
        super(in);
        this.pb = pb;
    }

    public ProgressBar getProgressBar() {
        return pb;
    }

    @Override
    public int read() throws IOException {
        int r = in.read();
        if (r != -1) pb.step();
        return r;
    }

    @Override
    public int read(char[] b) throws IOException {
        int r = in.read(b);
        if (r != -1) pb.stepBy(r);
        return r;
    }

    @Override
    public int read(char[] b, int off, int len) throws IOException {
        int r = in.read(b, off, len);
        if (r != -1) pb.stepBy(r);
        return r;
    }

    @Override
    public long skip(long n) throws IOException {
        long r = in.skip(n);
        pb.stepBy(r);
        return r;
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        in.mark(readAheadLimit);
        mark = pb.getCurrent();
    }

    @Override
    public void reset() throws IOException {
        in.reset();
        pb.stepTo(mark);
    }

    @Override
    public void close() throws IOException {
        in.close();
        pb.close();
    }
}
