package me.tongfei.progressbar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(StreamSetupExtension.class)
class Issue50Test {
    @Test
    void testCloseSpeed() throws Exception {
        int tenSecondsInMS = 10 * 1000;
        long startTime = System.currentTimeMillis();

        try(ProgressBar pb = new ProgressBarBuilder()
                .setTaskName("Foo").setInitialMax(100).setUpdateIntervalMillis(tenSecondsInMS).build()) {
            Thread.sleep(5);
        }

        long endTime = System.currentTimeMillis();

        assertTrue((endTime - startTime) < tenSecondsInMS);
    }
}
