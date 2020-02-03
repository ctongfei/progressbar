package me.tongfei.progressbar;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Default progress bar renderer (see {@link ProgressBarRenderer}).
 * @author Tongfei Chen
 * @since 0.8.0
 */
public class DefaultProgressBarRenderer implements ProgressBarRenderer {

    private ProgressBarStyle style;
    private String unitName;
    private long unitSize;
    private boolean isSpeedShown;
    private DecimalFormat speedFormat;
    private ChronoUnit speedUnit = ChronoUnit.SECONDS;

    DefaultProgressBarRenderer(
            ProgressBarStyle style,
            String unitName,
            long unitSize,
            boolean isSpeedShown,
            DecimalFormat speedFormat,
            ChronoUnit speedUnit
            
    ) {
        this.style = style;
        this.unitName = unitName;
        this.unitSize = unitSize;
        this.isSpeedShown = isSpeedShown;
        this.speedFormat = speedFormat;
        this.speedUnit = speedUnit;
    }

    // Number of full blocks
    private int progressIntegralPart(ProgressState progress, int length) {
        return (int)(progress.getNormalizedProgress() * length);
    }

    private int progressFractionalPart(ProgressState progress, int length) {
        double p = progress.getNormalizedProgress() * length;
        double fraction = (p - Math.floor(p)) * style.fractionSymbols.length();
        return (int) Math.floor(fraction);
    }

    private String eta(ProgressState progress, Duration elapsed) {
        if (progress.max <= 0 || progress.indefinite) return "?";
        else if (progress.current - progress.start == 0) return "?";
        else return Util.formatDuration(
                    elapsed.dividedBy(progress.current - progress.start).multipliedBy(progress.max - progress.current)
            );
    }

    private String percentage(ProgressState progress) {
        String res;
        if (progress.max <= 0 || progress.indefinite) res = "? %";
        else res = String.valueOf((int) Math.floor(100.0 * progress.current / progress.max)) + "%";
        return Util.repeat(' ', 4 - res.length()) + res;
    }

    private String ratio(ProgressState progress) {
        String m = progress.indefinite ? "?" : String.valueOf(progress.max / unitSize);
        String c = String.valueOf(progress.current / unitSize);
        return Util.repeat(' ', m.length() - c.length()) + c + "/" + m + unitName;
    }

    private String speed(ProgressState progress, Duration elapsed) {
        String suffix = null;
        double elapsedWithUnit = 0;
        if (null != speedUnit)
            switch (speedUnit) {
                case MINUTES:
                    suffix = "/m";
                    elapsedWithUnit = (double) elapsed.getSeconds() / (double) 60;
                    break;
                case HOURS:
                    suffix = "/h";
                    elapsedWithUnit = (double) elapsed.getSeconds() / (double) (60 * 60);
                    break;
                case DAYS:
                    suffix = "/d";
                    elapsedWithUnit = (double) elapsed.getSeconds() / (double) (60 * 60 * 24);
                    break;
                default:
                    suffix = "/s";
                    elapsedWithUnit = (double) elapsed.getSeconds();
                    break;
            }

        if (elapsed.getSeconds() == 0)
            return "?" + unitName + suffix;
        double speed = (double) (progress.current - progress.start) / elapsedWithUnit;
        double speedWithUnit = speed / unitSize;
        return speedFormat.format(speedWithUnit) + unitName + suffix;
    }

    public String render(ProgressState progress, int maxLength) {

        Instant currTime = Instant.now();
        Duration elapsed = Duration.between(progress.startTime, currTime);

        String prefix = progress.task + " " + percentage(progress) + " " + style.leftBracket;
        int maxSuffixLength = Math.max(maxLength - prefix.length(), 0);

        String speedString = isSpeedShown ? speed(progress, elapsed) : "";
        String suffix = style.rightBracket + " " + ratio(progress) + " ("
                + Util.formatDuration(elapsed) + " / " + eta(progress, elapsed) + ") "
                + speedString + progress.extraMessage;
        // trim excessive suffix
        if (suffix.length() > maxSuffixLength)
            suffix = suffix.substring(0, maxSuffixLength);

        int length = maxLength - prefix.length() - suffix.length();

        StringBuilder sb = new StringBuilder();
        sb.append(prefix);

        // case of indefinite progress bars
        if (progress.indefinite) {
            int pos = (int)(progress.current % length);
            sb.append(Util.repeat(style.space, pos));
            sb.append(style.block);
            sb.append(Util.repeat(style.space, length - pos - 1));
        }
        // case of definite progress bars
        else {
            sb.append(Util.repeat(style.block, progressIntegralPart(progress, length)));
            if (progress.current < progress.max) {
                sb.append(style.fractionSymbols.charAt(progressFractionalPart(progress, length)));
                sb.append(Util.repeat(style.space, length - progressIntegralPart(progress, length) - 1));
            }
        }

        sb.append(suffix);
        return sb.toString();
    }
}
