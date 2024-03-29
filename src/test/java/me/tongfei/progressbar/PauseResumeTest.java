package me.tongfei.progressbar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(StreamSetupExtension.class)
class PauseResumeTest {

    @Test
    void testPauseResume() {
        try (ProgressBar pb = new ProgressBarBuilder()
                .setTaskName("Test").setInitialMax(10).setUpdateIntervalMillis(100).setMaxRenderedLength(60).build()) {
            try {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(100);
                    pb.step();
                    Thread.sleep(100);
                    pb.step();
                    pb.pause();
                    Thread.sleep(1000);
                    pb.resume();
                }
            }
            catch (InterruptedException e) { }
        }
    }
}
