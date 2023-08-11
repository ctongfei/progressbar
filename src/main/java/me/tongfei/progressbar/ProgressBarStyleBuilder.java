package me.tongfei.progressbar;

/**
 * Builder for {@link ProgressBarStyle}s.
 *
 * @author Aleksandr Pakhomov
 * @since 0.10.0
 */
public class ProgressBarStyleBuilder {
    private static final String ESC_CODE = "\u001b[";

    private ProgressBarStyle style = ProgressBarStyle.ASCII;
    private byte colorCode = 0;

    /** Set refresh prompt. Default "\r". */
    public ProgressBarStyleBuilder refreshPrompt(String refreshPrompt) {
        style.refreshPrompt = refreshPrompt;
        return this;
    }

    /** Set left bracket. Default "[". */
    public ProgressBarStyleBuilder leftBracket(String leftBracket) {
        style.leftBracket = leftBracket;
        return this;
    }

    /** Set delimiting sequence. Default "". */
    public ProgressBarStyleBuilder delimitingSequence(String delimitingSequence) {
        style.delimitingSequence = delimitingSequence;
        return this;
    }

    /** Set right bracket. Default "]". */
    public ProgressBarStyleBuilder rightBracket(String rightBracket) {
        style.rightBracket = rightBracket;
        return this;
    }

    /** Set block character. Default "=" */
    public ProgressBarStyleBuilder block(char block) {
        style.block = block;
        return this;
    }

    /** Set space character. Default " " */
    public ProgressBarStyleBuilder space(char space) {
        style.space = space;
        return this;
    }

    /** Set fraction symbols. Default ">" */
    public ProgressBarStyleBuilder fractionSymbols(String fractionSymbols) {
        style.fractionSymbols = fractionSymbols;
        return this;
    }

    /** Set right side fraction symbol. Default ">" */
    public ProgressBarStyleBuilder rightSideFractionSymbol(char rightSideFractionSymbol) {
        style.rightSideFractionSymbol = rightSideFractionSymbol;
        return this;
    }

    /** Set ANSI color code. Default 0 (no color).  Must be in [0, 255]. */
    public ProgressBarStyleBuilder colorCode(byte code) {
        this.colorCode = code;
        return this;
    }

    /** Build {@link ProgressBarStyle}. */
    public ProgressBarStyle build() {
        boolean colorDefined = colorCode != 0;

        if (colorDefined && style.leftBracket.contains(ESC_CODE)) {
            throw new IllegalArgumentException("The color code is overridden with left bracket escape code. "
                    + "Please, remove the escape sequence from the left bracket or do not use color code.");
        }

        String prefix = colorDefined ? (ESC_CODE + colorCode + "m") : "";
        String postfix = colorDefined ? ESC_CODE + "0m" : "";
        style.leftBracket = prefix + style.leftBracket;
        style.rightBracket = style.rightBracket + postfix;
        return style;
    }

}
