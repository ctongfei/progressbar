package me.tongfei.progressbar;

/**
 * @author Tongfei Chen
 */
public class ProgressBarTest {

    static public void main(String[] args) throws Exception {
        ProgressBar pb = new ProgressBar("Test", 100, 100, System.out, ProgressBarStyle.UNICODE_BLOCK);
        pb.start();
        pb.setExtraMessage("xxxx");

        for (int i = 0; i < 100; i++) {
            Thread.sleep(3);
            pb.step();
            if (i == 300) {
                pb.setExtraMessage("a");
                pb.maxHint(1000);
            }
            if (i == 50) {
                pb.stepTo(80);
                pb.setExtraMessage("");
            }
        }
        pb.stop();
        System.out.println("Hello");
    }

}
