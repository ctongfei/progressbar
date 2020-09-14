package me.tongfei.progressbar;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Tongfei Chen
 */
public class ProgressBarTest {

    @Test
    public void test() {
        try (ProgressBar pb = new ProgressBarBuilder()
             .setTaskName("Test").setInitialMax(5).setUpdateIntervalMillis(50)
             .setStyle(ProgressBarStyle.UNICODE_BLOCK).setUnit("K", 1024).build()) {

            double x = 1.0;
            double y = x * x;

            ArrayList<Integer> l = new ArrayList<Integer>();

            System.out.println("\n\n\n\n\n");

            for (int i = 0; i < 10000; i++) {
                int sum = 0;
                for (int j = 0; j < i * 2000; j++)
                    sum += j;
                l.add(sum);

                pb.step();
                if (pb.getCurrent() > 8000) pb.maxHint(10000);

            }
        }
        System.out.println("Hello");
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
    @Test
    public void testSpeedUnit() throws InterruptedException {
        ProgressBar bar = new ProgressBarBuilder()
                .showSpeed(new DecimalFormat("#.####"))
                .setUnit("k", 1000)
                .setInitialMax(10000)
                .setSpeedUnit(ChronoUnit.MINUTES)
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
    public void testSpeedStartFrom() throws InterruptedException {
        ProgressBar bar = new ProgressBarBuilder()
                .showSpeed(new DecimalFormat("#.##"))
                .setUnit("k", 1000)
                .setInitialMax(10000)
                .startsFrom(5000, Duration.ZERO)
                .setUpdateIntervalMillis(10)
                .build();
        int x = 5000;
        while (x < 10000) {
            bar.step();
            Thread.sleep(1);
            x++;
        }

        bar.close();
    }

}
