package me.tongfei.progressbar;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Default progress bar renderer (see {@link ProgressBarRenderer}).
 * @author Tongfei Chen
 * @author Muhammet Sakarya
 * @since 0.8.0
 */
public class DefaultProgressBarRenderer implements ProgressBarRenderer {

    private ProgressBarStyle style;
    private String unitName;
    private long unitSize;
    private boolean isSpeedShown;
    private DecimalFormat speedFormat;
    private ChronoUnit speedUnit;

    protected DefaultProgressBarRenderer(
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
    protected int progressIntegralPart(ProgressState progress, int length) {
        return (int)(progress.getNormalizedProgress() * length);
    }

    protected int progressFractionalPart(ProgressState progress, int length) {
        double p = progress.getNormalizedProgress() * length;
        double fraction = (p - Math.floor(p)) * style.fractionSymbols.length();
        return (int) Math.floor(fraction);
    }

    protected String eta(ProgressState progress, Duration elapsed) {
        if (progress.max <= 0 || progress.indefinite) return "?";
        else if (progress.current - progress.start == 0) return "?";
        else return Util.formatDuration(
                    elapsed.dividedBy(progress.current - progress.start).multipliedBy(progress.max - progress.current)
            );
    }

    protected String percentage(ProgressState progress) {
        String res;
        if (progress.max <= 0 || progress.indefinite) res = "? %";
        else res = String.valueOf((int) Math.floor(100.0 * progress.current / progress.max)) + "%";
        return Util.repeat(' ', 4 - res.length()) + res;
    }

    protected String ratio(ProgressState progress) {
        String m = progress.indefinite ? "?" : String.valueOf(progress.max / unitSize);
        String c = String.valueOf(progress.current / unitSize);
        return Util.repeat(' ', m.length() - c.length()) + c + "/" + m + unitName;
    }

    protected String speed(ProgressState progress, Duration elapsed) {
        String suffix = "/s";
        double elapsedSeconds = elapsed.getSeconds();
        double elapsedInUnit = elapsedSeconds;
        if (null != speedUnit)
            switch (speedUnit) {
                case MINUTES:
                    suffix = "/min";
                    elapsedInUnit /= 60;
                    break;
                case HOURS:
                    suffix = "/h";
                    elapsedInUnit /= (60 * 60);
                    break;
                case DAYS:
                    suffix = "/d";
                    elapsedInUnit /= (60 * 60 * 24);
                    break;
            }

        if (elapsedSeconds == 0)
            return "?" + unitName + suffix;
        double speed = (double) (progress.current - progress.start) / elapsedInUnit;
        double speedWithUnit = speed / unitSize;
        return speedFormat.format(speedWithUnit) + unitName + suffix;
    }

    public String render(ProgressState progress, int maxLength) {

        Instant currTime = Instant.now();
        Duration elapsed = Duration.between(progress.startInstant, currTime);

        String prefix = progress.taskName + " " + percentage(progress) + " " + style.leftBracket;

        if (prefix.length() > maxLength)
            prefix = prefix.substring(0, maxLength - 1);

        // length of progress should be at least 1
        int maxSuffixLength = Math.max(maxLength - prefix.length() - 1, 0);

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
