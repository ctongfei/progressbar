package me.tongfei.progressbar;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PausedElapsedTest {
    @Test
    void elapsedTimeIsFrozenWhilePaused() throws InterruptedException {
        try (ProgressBar bar = ProgressBar.builder()
                .startsFrom(3, Duration.ofSeconds(2))
                .setInitialMax(10)
                .setConsumer(new DelegatingProgressBarConsumer(s -> { }, 80))
                .build()) {
            bar.pause();
            Duration pausedElapsed = bar.getElapsedBeforeStart();
            assertEquals(Duration.ZERO, bar.getElapsedAfterStart());
            assertEquals(pausedElapsed, bar.getTotalElapsed());
            Thread.sleep(20);
            assertEquals(pausedElapsed, bar.getTotalElapsed());
            bar.resume();
            Thread.sleep(20);
            assertTrue(bar.getTotalElapsed().compareTo(pausedElapsed) > 0);
        }
    }
}
