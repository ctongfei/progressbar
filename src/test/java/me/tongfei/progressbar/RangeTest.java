package me.tongfei.progressbar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.stream.IntStream;

/**
 * @author Tongfei Chen
 */
@ExtendWith(StreamSetupExtension.class)
class RangeTest {

    @Test
    void parallelRangeTest() {
        ProgressBar.wrap(IntStream.range(1000, 9000).parallel(), "Test parallel").forEach(i -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        });
    }

    @Test
    void sequentialRangeTest() {
        ProgressBar.wrap(IntStream.range(1000, 2000), "Test sequential").forEach(i -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        });
    }
}
