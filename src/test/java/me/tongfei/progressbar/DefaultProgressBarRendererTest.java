package me.tongfei.progressbar;

import java.text.DecimalFormat;
import java.time.Instant;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultProgressBarRendererTest {

    public final String taskName = "Test";
    private final int maxLength = 80;
    private final ProgressBarStyle style = ProgressBarStyle.ASCII;

    @Test
    public void basic() {
        ProgressState state = new ProgressState(taskName, 100);
        ProgressBarRenderer renderer = new DefaultProgressBarRenderer(style, "", 1, false, null);

        state.stepBy(10);
        state.setStartTime(Instant.now());
        String result = renderer.render(state, maxLength).get(0);

        String expected = taskName + "  10% [====>                                   ]  10/100 (0:00:00 / 0:00:00)";
        assertEquals(expected, result);

        // assert blank extra message
        state.setExtraMessage("   ");
        state.setStartTime(Instant.now());
        result = renderer.render(state, maxLength).get(0);
        assertEquals(expected, result);
    }

    @Test
    public void withSpeedBasicDecimalFormat() {
        ProgressState state = new ProgressState(taskName, 102400);
        ProgressBarRenderer renderer = new DefaultProgressBarRenderer(style, "Mb", 1024, true, new DecimalFormat("#"));

        state.stepBy(20480);
        state.setStartTime(Instant.now().minusSeconds(10));
        String result = renderer.render(state, maxLength).get(0);

        String expected = taskName + "  20% [======>                         ]  20/100Mb (0:00:10 / 0:00:39) 2Mb/s";
        assertEquals(expected, result);
    }

    @Test
    public void withSpeedAndDecimalFormat() {
        ProgressState state = new ProgressState(taskName, 10240);
        ProgressBarRenderer renderer = new DefaultProgressBarRenderer(style, "Mb", 1024, true, new DecimalFormat("#.#"));

        state.stepBy(2048);
        state.setExtraMessage("downloading..");
        state.setStartTime(Instant.now().minusSeconds(10));
        String result = renderer.render(state, maxLength).get(0);

        String expected = taskName + "  20% [===>              ]  2/10Mb (0:00:10 / 0:00:39) 0.2Mb/s downloading..";
        assertEquals(expected, result);
    }
}