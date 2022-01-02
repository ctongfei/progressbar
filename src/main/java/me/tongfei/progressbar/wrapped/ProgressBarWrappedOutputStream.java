package me.tongfei.progressbar.wrapped;

import me.tongfei.progressbar.ProgressBar;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProgressBarWrappedOutputStream extends FilterOutputStream {

    private ProgressBar pb;

    public ProgressBarWrappedOutputStream(OutputStream out, ProgressBar pb) {
        super(out);
        this.pb = pb;
    }

    public ProgressBar getProgressBar() {
        return pb;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
        pb.step();
    }

    @Override
    public void write(byte[] b) throws IOException {
        out.write(b, 0, b.length);
        pb.stepBy(b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
        pb.stepBy(len);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
        pb.refresh();
    }

    @Override
    public void close() throws IOException {
        out.close();
        pb.close();
    }
}
