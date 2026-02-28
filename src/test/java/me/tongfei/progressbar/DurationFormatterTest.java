package me.tongfei.progressbar;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DurationFormatterTest {

    @Test
    void testFormatWhenSeconds() {
        assertEquals("5", DurationFormatter.formatDuration(Duration.ofSeconds(5), TimeFormat.SECONDS));
        assertEquals("22", DurationFormatter.formatDuration(Duration.ofSeconds(22), TimeFormat.SECONDS));
        assertEquals("63", DurationFormatter.formatDuration(Duration.ofSeconds(63), TimeFormat.SECONDS));
        assertEquals("3682", DurationFormatter.formatDuration(Duration.ofSeconds(3682), TimeFormat.SECONDS));
    }

    @Test
    void testFormatWhenMinutesAndSeconds() {
        assertEquals("0:05", DurationFormatter.formatDuration(Duration.ofSeconds(5), TimeFormat.MINUTES_SECONDS));
        assertEquals("0:22", DurationFormatter.formatDuration(Duration.ofSeconds(22), TimeFormat.MINUTES_SECONDS));
        assertEquals("1:03", DurationFormatter.formatDuration(Duration.ofSeconds(63), TimeFormat.MINUTES_SECONDS));
        assertEquals("61:22", DurationFormatter.formatDuration(Duration.ofSeconds(3682), TimeFormat.MINUTES_SECONDS));
    }

    @Test
    void testFormatWhenAdapted() {
        assertEquals("5", DurationFormatter.formatDuration(Duration.ofSeconds(5), TimeFormat.ADAPTED));
        assertEquals("22", DurationFormatter.formatDuration(Duration.ofSeconds(22), TimeFormat.ADAPTED));
        assertEquals("1:03", DurationFormatter.formatDuration(Duration.ofSeconds(63), TimeFormat.ADAPTED));
        assertEquals("1:01:22", DurationFormatter.formatDuration(Duration.ofSeconds(3682), TimeFormat.ADAPTED));
    }

    @Test
    void testFormatWhenDefault() {
        assertEquals("0:00:05", DurationFormatter.formatDuration(Duration.ofSeconds(5), TimeFormat.DEFAULT));
        assertEquals("0:00:22", DurationFormatter.formatDuration(Duration.ofSeconds(22), TimeFormat.DEFAULT));
        assertEquals("0:01:03", DurationFormatter.formatDuration(Duration.ofSeconds(63), TimeFormat.DEFAULT));
        assertEquals("1:01:22", DurationFormatter.formatDuration(Duration.ofSeconds(3682), TimeFormat.DEFAULT));
    }
}