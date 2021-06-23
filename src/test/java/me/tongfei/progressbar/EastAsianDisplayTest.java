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
    void testEastAsianTrimDisplayLength() {
        assertEquals(StringDisplayUtils.trimDisplayLength("Progress bar", 0), "");
        assertEquals(StringDisplayUtils.trimDisplayLength("Progress bar", 10), "Progress b");
        assertEquals(StringDisplayUtils.trimDisplayLength("Progress bar", 15), "Progress bar");
        assertEquals(StringDisplayUtils.trimDisplayLength("进度条", 0), "");
        assertEquals(StringDisplayUtils.trimDisplayLength("进度条", 4), "进度");
        assertEquals(StringDisplayUtils.trimDisplayLength("进度条", 8), "进度条");
        assertEquals(StringDisplayUtils.trimDisplayLength("进度条", 0), "");
        assertEquals(StringDisplayUtils.trimDisplayLength("進度條", 4), "進度");
        assertEquals(StringDisplayUtils.trimDisplayLength("進度條", 8), "進度條");
        assertEquals(StringDisplayUtils.trimDisplayLength("진행 표시줄", 0), "");
        assertEquals(StringDisplayUtils.trimDisplayLength("진행 표시줄", 8), "진행 표");
        assertEquals(StringDisplayUtils.trimDisplayLength("진행 표시줄", 15), "진행 표시줄");
        assertEquals(StringDisplayUtils.trimDisplayLength("プログレスバー", 0), "");
        assertEquals(StringDisplayUtils.trimDisplayLength("プログレスバー", 11), "プログレス");
        assertEquals(StringDisplayUtils.trimDisplayLength("プログレスバー", 18), "プログレスバー");
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
