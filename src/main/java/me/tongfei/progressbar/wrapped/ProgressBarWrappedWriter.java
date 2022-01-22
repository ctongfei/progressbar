package me.tongfei.progressbar.wrapped;

import me.tongfei.progressbar.ProgressBar;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * A writer whose progress is tracked by a progress bar.
 * @since 0.9.3
 * @author Tongfei Chen
 */
public class ProgressBarWrappedWriter extends FilterWriter {

    private ProgressBar pb;

    public ProgressBarWrappedWriter(Writer out, ProgressBar pb) {
        super(out);
        this.pb = pb;
    }

    @Override
    public void write(int c) throws IOException {
        out.write(c);
        pb.step();
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        out.write(cbuf, off, len);
        pb.stepBy(len);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        out.write(str, off, len);
        pb.stepBy(len);
    }

    @Override
    public void write(String str) throws IOException {
        out.write(str);
        pb.stepBy(str.length());
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
