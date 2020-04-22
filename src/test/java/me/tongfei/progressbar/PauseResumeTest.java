package me.tongfei.progressbar;

import org.junit.Test;

public class PauseResumeTest {


    static public void main(String[] args) {
        try (ProgressBar pb = new ProgressBar("Test", 20, 100)) {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(100);
                    pb.step();
                    Thread.sleep(100);
                    pb.step();
                    pb.pause();
                    Thread.sleep(1000);
                    pb.resume();
                }
            }
            catch (InterruptedException e) { }
        }
    }
}
