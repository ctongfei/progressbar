package me.tongfei.progressbar;

import org.junit.Test;

import java.util.stream.IntStream;

/**
 * @author Tongfei Chen
 */
public class RangeTest {

    @Test
    public void parallelRangeTest() {
        ProgressBar.wrap(IntStream.range(1000, 2000).parallel(), "Test parallel").forEach(i -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        });
    }

    @Test
    public void sequentialRangeTest() {
        ProgressBar.wrap(IntStream.range(1000, 2000), "Test sequential").forEach(i -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        });
    }
}
