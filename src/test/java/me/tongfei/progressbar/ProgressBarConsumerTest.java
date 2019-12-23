package me.tongfei.progressbar;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alex Peelman
 */
public class ProgressBarConsumerTest {

    public static class SystemOutProgressBarConsumer implements ProgressBarConsumer {
        @Override
        public void beforeUpdate() {
            //NOOP
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
    public void printLoggerTest() throws InterruptedException {
        final Logger log = LoggerFactory.getLogger("Test");
        try (ProgressBar progressBar = new ProgressBarBuilder()
                .setInitialMax(100)
                .setTaskName("log.test")
                .setProgressBarConsumer(new DelegatingProgressBarConsumer(log::info))
                .setUpdateIntervalMillis(100)
                .build()) {
            for (int i=0; i < 100; i++) {
                progressBar.step();
                Thread.sleep(100);
            }
        }
    }

    //Must show speed, when set to 80 text will be truncated
    @Test
    public void toConsoleNonStandardWidthTest() throws InterruptedException {
        try (ProgressBar progressBar = new ProgressBarBuilder()
                .setInitialMax(100)
                .setTaskName("System.out.console.test")
                .setProgressBarConsumer(new ConsoleProgressBarConsumer(System.out))
                .setUpdateIntervalMillis(100)
                .showSpeed()
                .build()) {
            for (int i=0; i < 100; i++) {
                progressBar.step();
                Thread.sleep(100);
            }
        }
    }
}
