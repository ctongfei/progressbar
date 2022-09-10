package me.tongfei.progressbar;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.BiFunction;

import static me.tongfei.progressbar.StringDisplayUtils.*;

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
    private boolean isETAShown;
    private BiFunction<ProgressState, Duration, Optional<Duration>> eta;

    protected DefaultProgressBarRenderer(
            ProgressBarStyle style,
            String unitName,
            long unitSize,
            boolean isSpeedShown,
            DecimalFormat speedFormat,
            ChronoUnit speedUnit,
            boolean isETAShown,
            BiFunction<ProgressState, Duration, Optional<Duration>> eta
    ) {
        this.style = style;
        this.unitName = unitName;
        this.unitSize = unitSize;
        this.isSpeedShown = isSpeedShown;
        this.speedFormat = isSpeedShown && speedFormat == null ? new DecimalFormat() : speedFormat;
        this.speedUnit = speedUnit;
        this.isETAShown = isETAShown;
        this.eta = eta;
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

    protected String etaString(ProgressState progress, Duration elapsed) {
        Optional<Duration> eta = this.eta.apply(progress, elapsed);
        if (eta.isPresent()) {
            return Util.formatDuration(eta.get());
        }
        else {
            return "?";
        }
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
        if (maxLength <= 0) {
            return "";
        }

        Instant currTime = Instant.now();
        Duration elapsed = Duration.between(progress.startInstant, currTime);

        String prefix = progress.taskName + " " + percentage(progress) + " " + style.leftBracket;
        int prefixLength = getStringDisplayLength(prefix);

        if (prefixLength > maxLength) {
            prefix = trimDisplayLength(prefix, maxLength - 1);
            prefixLength = maxLength - 1;
        }

        // length of progress should be at least 1
        int maxSuffixLength = Math.max(maxLength - prefixLength - 1, 0);

        String speedString = isSpeedShown ? speed(progress, elapsed) : "";
        String suffix = style.rightBracket + " " + ratio(progress) + " ("
                + Util.formatDuration(elapsed) + (isETAShown ? " / " + etaString(progress, elapsed) : "") + ") "
                + speedString + progress.extraMessage;
        int suffixLength = getStringDisplayLength(suffix);
        // trim excessive suffix
        if (suffixLength > maxSuffixLength) {
            suffix = trimDisplayLength(suffix, maxSuffixLength);
            suffixLength = maxSuffixLength;
        }

        int length = maxLength - prefixLength - suffixLength;

        StringBuilder sb = new StringBuilder(maxLength);
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
