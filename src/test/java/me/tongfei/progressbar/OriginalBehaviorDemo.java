package me.tongfei.progressbar;

import org.junit.jupiter.api.Test;

/**
 * Minimal demo to capture original progress bar behavior for assignment screenshots.
 * Runs quickly and cleanly — does NOT modify the original test files.
 */
class OriginalBehaviorDemo {

    @Test
    void demoBasicProgressBar() throws InterruptedException {
        System.out.println("\n=== Demo 1: Basic Unicode Block Bar ===");
        try (ProgressBar pb = new ProgressBarBuilder()
                .setTaskName("Processing")
                .setInitialMax(10)
                .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK)
                .setUpdateIntervalMillis(200)
                .build()) {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(300);
                pb.step();
            }
        }
        System.out.println();
    }

    @Test
    void demoSpeedBar() throws InterruptedException {
        System.out.println("\n=== Demo 2: Bar with Speed Display ===");
        try (ProgressBar pb = new ProgressBarBuilder()
                .setTaskName("Downloading")
                .setInitialMax(10)
                .setStyle(ProgressBarStyle.ASCII)
                .setUnit("MB", 1)
                .showSpeed()
                .setUpdateIntervalMillis(200)
                .build()) {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(300);
                pb.step();
            }
        }
        System.out.println();
    }

    @Test
    void demoIndefiniteBar() throws InterruptedException {
        System.out.println("\n=== Demo 3: Indefinite (Unknown Max) Bar ===");
        try (ProgressBar pb = new ProgressBarBuilder()
                .setTaskName("Scanning")
                .setInitialMax(-1)    // -1 = indefinite, max is unknown
                .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BAR)
                .setUpdateIntervalMillis(200)
                .build()) {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(300);
                pb.step();
            }
        }
        System.out.println();
    }
}
