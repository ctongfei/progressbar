package me.tongfei.progressbar;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EastAsianDisplayTest {

    @Test
    void testEastAsianDisplayLength() {
        assertEquals(StringDisplayUtils.getStringDisplayLength("Progress bar"), 12);
        assertEquals(StringDisplayUtils.getStringDisplayLength("进度条"), 6);
        assertEquals(StringDisplayUtils.getStringDisplayLength("進度條"), 6);
        assertEquals(StringDisplayUtils.getStringDisplayLength("진행 표시줄"), 11);
        assertEquals(StringDisplayUtils.getStringDisplayLength("プログレスバー"), 14);
    }

    @Test
    void testProgressBarsWithEastAsianScript() {

        for (String name : new String[] {"Progress bar", "进度条", "進度條", "진행 표시줄", "プログレスバー"}) {
            ProgressBar.wrap(IntStream.range(0, 100), name).forEach(i -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            });
        }

    }

}
