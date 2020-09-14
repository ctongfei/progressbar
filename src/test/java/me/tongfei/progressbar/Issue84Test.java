package me.tongfei.progressbar;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Andrei Nakrasov
 */
public class Issue84Test {
    private final static int iterNumber = 100;
    private final static int criticalExtraMsgLen = 38;

    @Test
    public void testLongExtraMessage() {

        // redirect all exception messages to a new stream
        // https://stackoverflow.com/a/8708357
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.err;
        System.setErr(ps);

        try (ProgressBar pb = new ProgressBarBuilder()
                .setTaskName("Test")
                .setInitialMax(-1)
                .build()
                .setExtraMessage(Util.repeat('0', criticalExtraMsgLen))) {
            for (int i = 0; i < iterNumber; i++, pb.step()) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        String exceptionMsgChecker = baos.toString();
        System.setErr(old);
        // Exception is handled in ProgressThread run, so in test output is checked for exceptions
        assert (!exceptionMsgChecker.contains("Exception"));
    }
}
