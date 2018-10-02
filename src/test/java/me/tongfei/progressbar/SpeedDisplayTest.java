package me.tongfei.progressbar;

import org.junit.Test;

public class SpeedDisplayTest {
    @Test
    public void test() throws InterruptedException {
        ProgressBar bar = new ProgressBarBuilder()
                .showSpeed()
                .setUnit("k", 1000)
                .setInitialMax(10000)
                .build();
        int x = 0;
        while (x < 10000) {
            bar.step();
            Thread.sleep(1);
            x++;
        }

        bar.close();
    }
}
