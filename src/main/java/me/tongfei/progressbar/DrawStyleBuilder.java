package me.tongfei.progressbar;

/**
 * Builder for {@link DrawStyle}.
 *
 * @author Aleksandr Pakhomov
 */
public class DrawStyleBuilder {
    private static final String ESC_CODE = "\u001b[";

    private String refreshPrompt = "\r";
    private String leftBracket = "[";
    private String delimitingSequence = "";
    private String rightBracket = "]";
    private char block = '=';
    private char space = ' ';
    private String fractionSymbols = ">";
    private char rightSideFractionSymbol = '>';
    private int colorCode = 0;

    /** Set refresh prompt. Default "\r". */
    public DrawStyleBuilder refreshPrompt(String refreshPrompt) {
        this.refreshPrompt = refreshPrompt;
        return this;
    }

    /** Set left bracket. Default "[". */
    public DrawStyleBuilder leftBracket(String leftBracket) {
        this.leftBracket = leftBracket;
        return this;
    }

    /** Set delimiting sequence. Default "". */
    public DrawStyleBuilder delimitingSequence(String delimitingSequence) {
        this.delimitingSequence = delimitingSequence;
        return this;
    }

    /** Set right bracket. Default "]". */
    public DrawStyleBuilder rightBracket(String rightBracket) {
        this.rightBracket = rightBracket;
        return this;
    }

    /** Set block character. Default "=" */
    public DrawStyleBuilder block(char block) {
        this.block = block;
        return this;
    }

    /** Set space character. Default " " */
    public DrawStyleBuilder space(char space) {
        this.space = space;
        return this;
    }

    /** Set fraction symbols. Default ">" */
    public DrawStyleBuilder fractionSymbols(String fractionSymbols) {
        this.fractionSymbols = fractionSymbols;
        return this;
    }

    /** Set right side fraction symbol. Default ">" */
    public DrawStyleBuilder rightSideFractionSymbol(char rightSideFractionSymbol) {
        this.rightSideFractionSymbol = rightSideFractionSymbol;
        return this;
    }

    /** Set ANSI color code. Default 0 (no color).  Must be in [0, 255]. */
    public DrawStyleBuilder colorCode(int code) {
        if (code < 0 || code > 255)
            throw new IllegalArgumentException("Color code must be between 0 and 255.");

        this.colorCode = code;
        return this;
    }

    /** Build {@link DrawStyle}. */
    public DrawStyle build() {
        boolean colorDefined = colorCode != 0;

        if (colorDefined && leftBracket.contains(ESC_CODE)) {
            throw new IllegalArgumentException("The color code is overridden with left bracket escape code. "
                    + "Please, remove the escape sequence from the left bracket or do not use color code.");
        }

        String prefix = colorDefined ? (ESC_CODE + colorCode + "m") : "";
        String postfix = colorDefined ? ESC_CODE + "0m" : "";

        return new InternalDrawStyle(refreshPrompt, prefix + leftBracket, delimitingSequence,
                rightBracket + postfix, block, space, fractionSymbols, rightSideFractionSymbol);
    }

    /** Apply {@link ProgressBarStyle}. */
    public DrawStyleBuilder apply(ProgressBarStyle style) {
        refreshPrompt(style.refreshPrompt);
        leftBracket(style.leftBracket);
        delimitingSequence(style.delimitingSequence);
        rightBracket(style.rightBracket);
        block(style.block);
        space(style.space);
        fractionSymbols(style.fractionSymbols);
        return rightSideFractionSymbol(style.rightSideFractionSymbol);
    }

    static class InternalDrawStyle implements DrawStyle {
        private final String refreshPrompt;
        private final String leftBracket;
        private final String delimitingSequence;
        private final String rightBracket;
        private final char block;
        private final char space;
        private final String fractionSymbols;
        private final char rightSideFractionSymbol;

        private InternalDrawStyle(String refreshPrompt, String leftBracket, String delimitingSequence,
                String rightBracket, char block, char space, String fractionSymbols, char rightSideFractionSymbol) {
            this.refreshPrompt = refreshPrompt;
            this.leftBracket = leftBracket;
            this.delimitingSequence = delimitingSequence;
            this.rightBracket = rightBracket;
            this.block = block;
            this.space = space;
            this.fractionSymbols = fractionSymbols;
            this.rightSideFractionSymbol = rightSideFractionSymbol;
        }

        @Override
        public String refreshPrompt() {
            return refreshPrompt;
        }

        @Override
        public String leftBracket() {
            return leftBracket;
        }

        @Override
        public String delimitingSequence() {
            return delimitingSequence;
        }

        @Override
        public String rightBracket() {
            return rightBracket;
        }

        @Override
        public char block() {
            return block;
        }

        @Override
        public char space() {
            return space;
        }

        @Override
        public String fractionSymbols() {
            return fractionSymbols;
        }

        @Override
        public char rightSideFractionSymbol() {
            return rightSideFractionSymbol;
        }
    }
}
