package me.tongfei.progressbar;

import org.junit.Test;

public class ProgressBarConsumerTest {

    public static class SystemOutProgressBarConsumer implements ProgressBarConsumer{
        @Override
        public void beforeUpdate() {
            //NOOP
        }

        @Override
        public int getMaxSuffixLength(int prefixLength) {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getMaxProgressLength() {
            return 100;
        }

        @Override
        public void accept(String progressBar) {
            System.out.println(progressBar);
        }

        @Override
        public void close() {
            //NOOP
        }
    }

    //Prints progress as new lines without carriage return
    @Test
    public void toSystemOutTest() throws InterruptedException {
        try (ProgressBar progressBar = new ProgressBarBuilder()
                .setInitialMax(100)
                .setTaskName("System.out.println.test")
                .setProgressBarConsumer(new SystemOutProgressBarConsumer())
                .setUpdateIntervalMillis(100)
                .showSpeed()
                .build()) {
            for (int i=0; i < 100; i++) {
                progressBar.step();
                Thread.sleep(15);
            }
        }
    }

    //Must show speed, when set to 80 text will be truncated
    @Test
    public void toConsoleNonStandardWidthTest() throws InterruptedException {
        try (ProgressBar progressBar = new ProgressBarBuilder()
                .setInitialMax(100)
                .setTaskName("System.out.console.test")
                .setProgressBarConsumer(new ConsoleLogger(System.out, 100))
                .setUpdateIntervalMillis(100)
                .showSpeed()
                .build()) {
            for (int i=0; i < 100; i++) {
                progressBar.step();
                Thread.sleep(15);
            }
        }
    }
}
