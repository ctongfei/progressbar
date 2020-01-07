package me.tongfei.progressbar;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;

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

    DefaultProgressBarRenderer(
            ProgressBarStyle style,
            String unitName,
            long unitSize,
            boolean isSpeedShown,
            DecimalFormat speedFormat
    ) {
        this.style = style;
        this.unitName = unitName;
        this.unitSize = unitSize;
        this.isSpeedShown = isSpeedShown;
        this.speedFormat = speedFormat;
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
        else if (progress.current == 0) return "?";
        else return Util.formatDuration(
                    elapsed.dividedBy(progress.current).multipliedBy(progress.max - progress.current)
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
        if (elapsed.getSeconds() == 0) return "?" + unitName + "/s";
        double speed = (double) progress.current / elapsed.getSeconds();
        double speedWithUnit = speed / unitSize;
        return speedFormat.format(speedWithUnit) + unitName + "/s";
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
