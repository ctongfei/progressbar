package me.tongfei.progressbar;

/**
 * A draw style for a progress bar.
 *
 * @author Aleksandr Pakhomov
 */
public interface DrawStyle {
    /** Create draw style object from {@link ProgressBarStyle} */
    static DrawStyle from(ProgressBarStyle style) {
        return builder().apply(style).build();
    }

    /** Symbol to refresh the progress bar. */
    String refreshPrompt();

    /** Left bracket of the progress bar. */
    String leftBracket();

    /** Delimiting sequence between the progress bar and the status text. */
    String delimitingSequence();

    /** Right bracket of the progress bar. */
    String rightBracket();

    /** Block character of the progress bar. [=====>     ]  here '=' is the block. */
    char block();

    /** Space character of the progress bar. [=====>     ]  here ' ' is the space. */
    char space();

    /** Fraction symbols of the progress bar.  */
    String fractionSymbols();

    /** Right side fraction symbol of the progress bar. [=====>      ] here '>'. */
    char rightSideFractionSymbol();

    /** Create a new builder for {@link DrawStyle} */
    static DrawStyleBuilder builder() {
        return new DrawStyleBuilder();
    }
}
