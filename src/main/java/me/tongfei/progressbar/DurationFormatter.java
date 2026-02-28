package me.tongfei.progressbar;

import java.time.Duration;

public class DurationFormatter {

    private DurationFormatter() {
    }

    public static String formatDuration(Duration d) {
        return formatDuration(d, TimeFormat.DEFAULT);
    }

    public static String formatDuration(Duration d, TimeFormat format) {
        final long s = d.getSeconds();
        switch (format) {
            case SECONDS:
                return formatAsSeconds(s);
            case MINUTES_SECONDS:
                return formatAsMinutesAndSeconds(s);
            case ADAPTED: {
                if (s < 60) {
                    return formatAsSeconds(s);
                } else if (s < 3600) {
                    return formatAsMinutesAndSeconds(s);
                } else {
                    return formatAsHoursMinutedAndSeconds(s);
                }
            }
            case DEFAULT:
            default:
                return formatAsHoursMinutedAndSeconds(s);
        }
    }

    private static String formatAsSeconds(long s) {
        return String.format("%d", s);
    }

    private static String formatAsMinutesAndSeconds(long s) {
        return String.format("%d:%02d", s / 60, s % 60);
    }

    private static String formatAsHoursMinutedAndSeconds(long s) {
        return String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60);
    }

}
