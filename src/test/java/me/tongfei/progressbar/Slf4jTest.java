package me.tongfei.progressbar;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alex Peelman
 */
class Slf4jTest {

    //Prints progress as new lines without carriage return
    @Test
    void printLoggerTest() throws InterruptedException {
        final Logger logger = LoggerFactory.getLogger("Test");
        try (ProgressBar pb = new ProgressBarBuilder()
                .setInitialMax(100)
                .setTaskName("log.test")
                .setConsumer(new DelegatingProgressBarConsumer(logger::info))
                .setUpdateIntervalMillis(1)
                .build()) {
            for (int i = 0; i < 100; i++) {
                pb.step();
                Thread.sleep(10);
            }
        }
    }

}
