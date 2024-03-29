package me.tongfei.progressbar;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** @author Aleksandr Pakhomov */
public class CustomProgressBarTest {

    ProgressBarBuilder builder;

    private static void simulateProgress(ProgressBar bar) throws InterruptedException {
        int x = 0;
        while (x < 10000) {
            bar.step();
            Thread.sleep(1);
            x++;
        }
    }

    @BeforeEach
    void setUp() {
        builder = new ProgressBarBuilder()
                .setUnit("k", 1000)
                .setInitialMax(10000);
    }

    @Test
    void defaultProgressBarStyle() throws InterruptedException {
        // Given default
        try (ProgressBar bar = builder.build()) {
            // Expect display default progress bar style:
            // 100% │███████████████████████████████████████████│ 10/10k (0:00:12 / 0:00:00)
            simulateProgress(bar);
        }
    }

    @Test
    void customPredefinedProgressBarStyle() throws InterruptedException {
        // Given ASCII progress bar style that is taken from ProgressBarStyle enum
        builder.setStyle(ProgressBarStyle.ASCII);
        try (ProgressBar bar = builder.build()) {
            // Expect display custom progress bar style:
            // 50% [=================>                 ] 5/10k (0:00:06 / 0:00:12)
            simulateProgress(bar);
        }
    }

    @Test
    void customUserDefinedProgressBarStyleWithColor() throws InterruptedException {
        // Given custom progress bar style
        builder.setStyle(
                ProgressBarStyle.builder()
                        .colorCode((byte) 36)
                        .leftBracket("{")
                        .rightBracket("}")
                        .block('-')
                        .rightSideFractionSymbol('+')
                        .build()
        );
        try (ProgressBar bar = builder.build()) {
            // Expect display custom progress bar style:
            // 50% {-------------------+               } 5/10k (0:00:06 / 0:00:12)
            simulateProgress(bar);
        }
    }

    @Test
    void customColorCannotBeUsedWithEscapeSymbol() {
        // Given draw style with both color code and escape symbols
        ProgressBarStyleBuilder drawStyleBuilder = ProgressBarStyle.builder()
                .colorCode((byte) 33) // yellow
                .leftBracket("\u001b[36m{");  // but this overrides color code

        // Expect
        assertThrows(IllegalArgumentException.class, drawStyleBuilder::build);
    }

}
