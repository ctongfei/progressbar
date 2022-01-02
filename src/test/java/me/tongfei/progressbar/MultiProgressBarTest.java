package me.tongfei.progressbar;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(StreamSetupExtension.class)
class MultiProgressBarTest {

    @Test
    void testMultiProgressBar() throws InterruptedException {
        try (
                ProgressBar pb1 = new ProgressBar("PB1", 100);
                ProgressBar pb2 = new ProgressBar("PB2", 100)
        ) {
            for (int i = 0; i < 100; i++) {
                pb1.step();
                pb2.step();
                Thread.sleep(100);
            }
        }

    }

}
