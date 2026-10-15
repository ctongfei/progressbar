package me.tongfei.progressbar;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WrappedStreamCloseTest {

    @Test
    void closesSequentialFileStream() throws IOException {
        closesFileStream(false);
    }

    @Test
    void closesParallelFileStream() throws IOException {
        closesFileStream(true);
    }

    private void closesFileStream(boolean parallel) throws IOException {
        Path file = Files.createTempFile("progressbar-stream", ".txt");
        Files.write(file, Arrays.asList("first", "second"), StandardCharsets.UTF_8);
        AtomicBoolean closed = new AtomicBoolean();
        Stream<String> source = Files.lines(file, StandardCharsets.UTF_8)
                .onClose(() -> closed.set(true));
        try {
            if (parallel) source = source.parallel();
            ProgressBarBuilder builder = new ProgressBarBuilder()
                    .setConsumer(new DelegatingProgressBarConsumer(s -> { }, 80));
            try (Stream<String> wrapped = ProgressBar.wrap(source, builder)) {
                assertEquals(parallel, wrapped.isParallel());
                assertEquals(Arrays.asList("first", "second"), wrapped.collect(Collectors.toList()));
            }
            assertTrue(closed.get(), "Closing the wrapper must close the underlying file stream");
            Files.delete(file);
        } finally {
            source.close();
            Files.deleteIfExists(file);
        }
    }
}
