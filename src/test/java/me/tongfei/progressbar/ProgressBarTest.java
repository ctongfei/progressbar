package me.tongfei.progressbar;

/**
 * @author Tongfei Chen (ctongfei@gmail.com).
 */
public class ProgressBarTest {

    static public void main(String[] args) throws Exception {
        ProgressBar pb = new ProgressBar("Test", 10, 100);
        pb.start();
        pb.setExtraMessage("xxxx");

        for (int i = 0; i < 10000; i++) {
            double[][] x = new double[1000][];
            Thread.sleep(3);
            pb.step();
            if (i == 300) {
                pb.setExtraMessage("a");
                pb.maxHint(10000);
            }
            if (i == 5000) {
                pb.stepTo(8000);
                pb.setExtraMessage("");
            }
        }
        pb.stop();
    }

}
