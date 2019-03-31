package me.tongfei.progressbar;

import org.junit.Test;

import java.text.DecimalFormat;


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

    @Test
    public void testSpeedFormat() throws InterruptedException {
        ProgressBar bar = new ProgressBarBuilder()
                .showSpeed(new DecimalFormat("#.##"))
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
