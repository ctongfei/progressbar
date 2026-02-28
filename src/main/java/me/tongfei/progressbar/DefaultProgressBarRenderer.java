package me.tongfei.progressbar;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Function;

import static me.tongfei.progressbar.StringDisplayUtils.getStringDisplayLength;
import static me.tongfei.progressbar.StringDisplayUtils.trimDisplayLength;

/**
 * Default progress bar renderer (see {@link ProgressBarRenderer}).
 *
 * @author Tongfei Chen
 * @author Muhammet Sakarya
 * @since 0.8.0
 */
public class DefaultProgressBarRenderer implements ProgressBarRenderer {

    private final ProgressBarStyle style;

    private final String unitName;

    private final long unitSize;

    private final boolean isSpeedShown;

    private final TimeFormat timeFormat;

    private final DecimalFormat speedFormat;

    private final ChronoUnit speedUnit;

    private final boolean isEtaShown;

    private final Function<ProgressState, Optional<Duration>> eta;

    protected DefaultProgressBarRenderer(
            ProgressBarStyle style,
            String unitName,
            long unitSize,
            boolean isSpeedShown,
            DecimalFormat speedFormat,
            ChronoUnit speedUnit,
            boolean isEtaShown,
            Function<ProgressState, Optional<Duration>> eta,
            TimeFormat timeFormat
    ) {
        this.style = style;
        this.unitName = unitName;
        this.unitSize = unitSize;
        this.isSpeedShown = isSpeedShown;
        this.timeFormat = timeFormat;
        this.speedFormat = isSpeedShown && speedFormat == null ? new DecimalFormat() : speedFormat;
        this.speedUnit = speedUnit;
        this.isEtaShown = isEtaShown;
        this.eta = eta;
    }

    // Number of full blocks
    protected int progressIntegralPart(ProgressState progress, int length) {
        return (int) (progress.getNormalizedProgress() * length);
    }

    protected int progressFractionalPart(ProgressState progress, int length) {
        double p = progress.getNormalizedProgress() * length;
        double fraction = (p - Math.floor(p)) * style.fractionSymbols.length();
        return (int) Math.floor(fraction);
    }

    protected String etaString(ProgressState progress) {
        final Optional<Duration> optEta = this.eta.apply(progress);
        if (optEta.isPresent()) {
            return DurationFormatter.formatDuration(optEta.get(), timeFormat);
        } else {
            return "?";
        }
    }

    protected String percentage(ProgressState progress) {
        String res;
        if (progress.max <= 0 || progress.indefinite) {
            res = "? %";
        } else {
            res = String.valueOf((int) Math.floor(100.0 * progress.current / progress.max)) + "%";
        }
        return Util.repeat(' ', 4 - res.length()) + res;
    }

    protected String ratio(ProgressState progress) {
        String m = progress.indefinite ? "?" : String.valueOf(progress.max / unitSize);
        String c = String.valueOf(progress.current / unitSize);
        return Util.repeat(' ', m.length() - c.length()) + c + "/" + m + unitName;
    }

    protected String speed(ProgressState progress) {
        String suffix = "/s";
        double elapsedSeconds = progress.getElapsedAfterStart().getSeconds();
        double elapsedInUnit = elapsedSeconds;
        if (null != speedUnit) {
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
        }

        if (elapsedSeconds == 0) {
            return "?" + unitName + suffix;
        }
        double speed = (double) (progress.current - progress.start) / elapsedInUnit;
        double speedWithUnit = speed / unitSize;
        return speedFormat.format(speedWithUnit) + unitName + suffix;
    }

    public String render(ProgressState progress, int maxLength) {
        if (maxLength <= 0) {
            return "";
        }

        String prefix = progress.getTaskName() + " " + percentage(progress) + " " + style.leftBracket;
        int prefixLength = getStringDisplayLength(prefix);

        if (prefixLength > maxLength) {
            prefix = trimDisplayLength(prefix, maxLength - 1);
            prefixLength = maxLength - 1;
        }

        // length of progress should be at least 1
        int maxSuffixLength = Math.max(maxLength - prefixLength - 1, 0);

        String speedString = isSpeedShown ? speed(progress) : "";
        String suffix = style.rightBracket + " " + ratio(progress) + " ("
                + DurationFormatter.formatDuration(progress.getTotalElapsed(), timeFormat)
                + (isEtaShown ? " / " + etaString(progress) : "")
                + ") "
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
            int pos = (int) (progress.current % length);
            sb.append(Util.repeat(style.space, pos));
            sb.append(style.block);
            sb.append(Util.repeat(style.space, length - pos - 1));
        }
        // case of definite progress bars
        else {
            sb.append(Util.repeat(style.block, progressIntegralPart(progress, length)));
            if (progress.current < progress.max) {
                int fraction = progressFractionalPart(progress, length);
                if (fraction != 0) {
                    sb.append(style.fractionSymbols.charAt(fraction));
                    sb.append(style.delimitingSequence);
                } else {
                    sb.append(style.delimitingSequence);
                    sb.append(style.rightSideFractionSymbol);
                }
                sb.append(Util.repeat(style.space, length - progressIntegralPart(progress, length) - 1));
            }
        }

        sb.append(suffix);
        return sb.toString();
    }
}
