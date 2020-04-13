package me.tongfei.progressbar;

/**
 * Represents the display style of a progress bar.
 * @author Tongfei Chen
 * @since 0.5.1
 */
public enum ProgressBarStyle {

    COLORFUL_UNICODE_BLOCK("\r", "\u001b[33m│", "│\u001b[0m", '█', ' ', " ▏▎▍▌▋▊▉", " ┣━ ", " ┗━ ", " ┃ "),

    /** Use Unicode block characters to draw the progress bar. */
    UNICODE_BLOCK("\r", "│", "│", '█', ' ', " ▏▎▍▌▋▊▉", " ┣━ ", " ┗━ ", " ┃ "),

    /** Use only ASCII characters to draw the progress bar. */
    ASCII("\r", "[", "]", '=', ' ', ">", " |- ", " '- ", " | ");

    String refreshPrompt;
    String leftBracket;
    String rightBracket;
    char block;
    char space;
    String fractionSymbols;
    String verticalRight;
    String upRight;
    String vertical;

    ProgressBarStyle(String refreshPrompt, String leftBracket, String rightBracket, char block, char space, String fractionSymbols, String verticalRight, String upRight, String vertical) {
        this.refreshPrompt = refreshPrompt;
        this.leftBracket = leftBracket;
        this.rightBracket = rightBracket;
        this.block = block;
        this.space = space;
        this.fractionSymbols = fractionSymbols;
        this.verticalRight = verticalRight;
        this.upRight = upRight;
        this.vertical = vertical;
    }

}
