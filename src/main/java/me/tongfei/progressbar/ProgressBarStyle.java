package me.tongfei.progressbar;

/**
 * Represents the display style of a progress bar.
 * @author Tongfei Chen
 * @since 0.5.1
 */
public enum ProgressBarStyle {

    COLORFUL_UNICODE_BLOCK("\r", "\u001b[33m│", "", "│\u001b[0m", '█', ' ', " ▏▎▍▌▋▊▉", ' '),

    COLORFUL_UNICODE_BAR("\r", "\u001b[33m", "\u001b[90m", "\u001b[0m", '━', '━', " ╸", '╺'),

    /** Use Unicode block characters to draw the progress bar. */
    UNICODE_BLOCK("\r", "|", "", "|", '█', ' ', " ▏▎▍▌▋▊▉", ' '),

    /** Use only ASCII characters to draw the progress bar. */
    ASCII("\r", "[", "", "]", '=', ' ', ">", ' ');

    String refreshPrompt;
    String leftBracket;
    String delimitingSequence;
    String rightBracket;
    char block;
    char space;
    String fractionSymbols;
    char rightSideFractionSymbol;

    ProgressBarStyle(String refreshPrompt, String leftBracket, String delimitingSequence, String rightBracket, char block, char space, String fractionSymbols, char rightSideFractionSymbol) {
        this.refreshPrompt = refreshPrompt;
        this.leftBracket = leftBracket;
        this.delimitingSequence = delimitingSequence;
        this.rightBracket = rightBracket;
        this.block = block;
        this.space = space;
        this.fractionSymbols = fractionSymbols;
        this.rightSideFractionSymbol = rightSideFractionSymbol;
    }

}
