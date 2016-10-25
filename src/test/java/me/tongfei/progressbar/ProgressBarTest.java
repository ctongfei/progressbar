package me.tongfei.progressbar;

/**
 * @author Tongfei Chen
 */
public class ProgressBarTest {

    static public void main(String[] args) throws Exception {
        ProgressBar pb = new ProgressBar("Test", 100);
        pb.start();
        pb.setExtraMessage("xxxx");

        for (int i = 0; i < 10000; i++) {
            Thread.sleep(30);
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
        if(pb.getCurrent() != 12999)
            throw new Exception("fail!");
    }

}
