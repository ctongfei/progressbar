package me.tongfei.progressbar;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MockProgressBarBuilder extends ProgressBarBuilder {

    public final String taskName = "Test";
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    public MockProgressBarBuilder() {
        try {
            setTaskName(taskName);
            setStyle(ProgressBarStyle.ASCII);
            setUpdateIntervalMillis(100);
            setConsumer(new InteractiveConsoleProgressBarConsumer(new PrintStream(out, true, UTF_8.name())));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("This should never happen!");
        }
    }

    public String getOutput() {
        try {
            return out.toString(UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("This should never happen!");
        }
    }
}
