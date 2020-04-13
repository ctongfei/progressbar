package me.tongfei.progressbar;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Default progress bar renderer (see {@link ProgressBarRenderer}).
 *
 * @author Tongfei Chen
 * @since 0.8.0
 */
public class DefaultProgressBarRenderer implements ProgressBarRenderer {

    private final ProgressBarStyle style;
    private final String unitName;
    private final long unitSize;
    private final boolean isSpeedShown;
    private final DecimalFormat speedFormat;

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
    private int progressIntegralPart(ProgressStateImmutable progress, int length) {
        return (int) (progress.getNormalizedProgress() * length);
    }

    private int progressFractionalPart(ProgressStateImmutable progress, int length) {
        double p = progress.getNormalizedProgress() * length;
        double fraction = (p - Math.floor(p)) * style.fractionSymbols.length();
        return (int) Math.floor(fraction);
    }

    private String eta(ProgressStateImmutable progress) {
        if (progress.max <= 0 || progress.indefinite) return "?";
        else if (!progress.getChildren().isEmpty()) return Util.formatDuration(progress.eta());
        else if (progress.current == 0) return "?";
        else return Util.formatDuration(progress.eta());
    }

    private String percentage(ProgressStateImmutable progress) {
        String res;
        if (progress.max <= 0 || progress.indefinite) res = "? %";
        else res = progress.progress() + "%";
        return Util.repeat(' ', 4 - res.length()) + res;
    }

    private String ratio(ProgressStateImmutable progress) {
        String m = progress.indefinite ? "?" : String.valueOf(progress.max / unitSize);
        String c = String.valueOf(progress.current / unitSize);
        return Util.repeat(' ', m.length() - c.length()) + c + "/" + m + unitName;
    }

    private String speed(ProgressStateImmutable progress) {
        if (progress.elapsed.getSeconds() == 0) return "?" + unitName + "/s";
        double speed = progress.speed();
        double speedWithUnit = speed / unitSize;
        return speedFormat.format(speedWithUnit) + unitName + "/s";
    }

    public List<String> render(ProgressState progress, int maxLength) {
        return render(progress.getState(), maxLength, 0, false);
    }

    private List<String> render(ProgressStateImmutable progress, int maxLength, int subChild, boolean isLast) {
        LinkedList<String> result = new LinkedList<>();
        List<ProgressStateImmutable> children = progress.getChildren();
        for (ProgressStateImmutable childState : children) {
            List<String> render = render(childState, maxLength, subChild + 1, children.get(children.size() - 1) == childState);
            result.addAll(render);
        }

        result.add(0, doRender(progress, maxLength, subChild, isLast));
        return result;
    }

    private String doRender(ProgressStateImmutable progress, int maxLength, int subChild, boolean isLast) {

        String prefix = "";
        for (int i = 0; i < subChild; i++) {
            if (i == subChild - 1) {
                if (isLast) {
                    prefix += style.upRight;
                } else {
                    prefix += style.verticalRight;
                }
            } else {
                prefix += style.vertical;
            }
        }

        prefix += progress.task + " " + percentage(progress) + " " + style.leftBracket;

        int maxSuffixLength = Math.max(maxLength - prefix.length(), 0);

        String speedString = isSpeedShown ? " " + speed(progress) : "";
        String suffix = style.rightBracket + " " + ratio(progress) + " ("
                + Util.formatDuration(progress.elapsed) + " / " + eta(progress) + ")"
                + speedString + (progress.extraMessage.isEmpty() ? "" : " " + progress.extraMessage);
        // trim excessive suffix
        if (suffix.length() > maxSuffixLength)
            suffix = suffix.substring(0, maxSuffixLength);

        int length = maxLength - prefix.length() - suffix.length();

        StringBuilder sb = new StringBuilder();
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
                sb.append(style.fractionSymbols.charAt(progressFractionalPart(progress, length)));
                sb.append(Util.repeat(style.space, length - progressIntegralPart(progress, length) - 1));
            }
        }

        sb.append(suffix);
        return sb.toString();
    }
}
