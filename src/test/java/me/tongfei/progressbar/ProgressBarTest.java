package me.tongfei.progressbar;

/**
 * @author Tongfei Chen (ctongfei@gmail.com).
 */
public class ProgressBarTest {

    static public void main(String[] args) {
        ProgressBar pb = new ProgressBar("Test", 10);
        pb.start();
        pb.setExtraMessage("xxxx");

        for (int i = 0; i < 10000; i++) {
            double[][] x = new double[1000][];
            for (int j = 0; j < 1000; j++) {
                x[j] = new double[1000];
                for (int k = 0; k < 1000; k++)
                    x[j][k] = j + 0.1324 * k;
            }
            pb.step();
            if (i == 300) {
                pb.setExtraMessage("a");
                pb.maxHint(10000);
            }
            if (i == 5000) {
                pb.stepTo(8000);
            }
        }
        try {
            pb.setOutputStream(ProgressBar.OutputStream.ERR);
            throw new AssertionError("Setting the output stream didn't throw!");
        } catch (IllegalStateException ex) {}
        pb.stop();
    }

}
