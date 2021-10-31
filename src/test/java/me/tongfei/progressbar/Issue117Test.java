package me.tongfei.progressbar;

import org.junit.jupiter.api.Test;

public class Issue117Test {

    @Test
    void testImmediateStart() throws InterruptedException {
        try (ProgressBar pb = new ProgressBar("Test", 2)) {
            for (int i = 0; i < 2; i++) {
                Thread.sleep(1000);
                pb.step();
            }
        }
    }

}
