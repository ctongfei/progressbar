package me.tongfei.progressbar;

import me.tongfei.progressbar.wrapped.ProgressBarWrappedInputStream;
import me.tongfei.progressbar.wrapped.ProgressBarWrappedOutputStream;
import me.tongfei.progressbar.wrapped.ProgressBarWrappedReader;
import me.tongfei.progressbar.wrapped.ProgressBarWrappedWriter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.FilterReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WrappedCloseFailureTest {
    @Test
    void inputStreamCloseFailureClosesProgressBar() throws Exception {
        assertCleanup((pb, failure) -> new ProgressBarWrappedInputStream(new ByteArrayInputStream(new byte[0]) {
            @Override
            public void close() throws IOException { throw failure; }
        }, pb));
    }

    @Test
    void outputStreamCloseFailureClosesProgressBar() throws Exception {
        assertCleanup((pb, failure) -> new ProgressBarWrappedOutputStream(new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException { throw failure; }
        }, pb));
    }

    @Test
    void readerCloseFailureClosesProgressBar() throws Exception {
        assertCleanup((pb, failure) -> new ProgressBarWrappedReader(new FilterReader(new StringReader("")) {
            @Override
            public void close() throws IOException { throw failure; }
        }, pb));
    }

    @Test
    void writerCloseFailureClosesProgressBar() throws Exception {
        assertCleanup((pb, failure) -> new ProgressBarWrappedWriter(new StringWriter() {
            @Override
            public void close() throws IOException { throw failure; }
        }, pb));
    }

    private void assertCleanup(BiFunction<ProgressBar, IOException, Closeable> wrapper) throws Exception {
        AtomicBoolean progressBarClosed = new AtomicBoolean();
        ProgressBarConsumer consumer = new ProgressBarConsumer() {
            public int getMaxRenderedLength() { return 80; }
            public void accept(String rendered) { }
            public void close() { }
        };
        ProgressBar pb = new ProgressBar("close", 1, 1000, false, false, 0,
                Duration.ZERO, (state, maxLength) -> "", consumer) {
            @Override
            public void close() {
                progressBarClosed.set(true);
                super.close();
            }
        };
        IOException failure = new IOException("close failed");
        try {
            Closeable stream = wrapper.apply(pb, failure);
            assertSame(failure, assertThrows(IOException.class, stream::close));
            assertTrue(progressBarClosed.get(), "The progress bar must close even if the wrapped resource fails");
        } finally {
            pb.close();
        }
    }
}
