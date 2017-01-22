package me.tongfei.progressbar;

import java.util.ArrayList;

/**
 * @author Tongfei Chen
 */
public class ProgressBarTest {

    static public void main(String[] args) throws Exception {
        ProgressBar pb = new ProgressBar("Test", 5000, 100, System.out, ProgressBarStyle.UNICODE_BLOCK).start();

        double x = 1.0;
        double y = x * x;
        

        ArrayList<Integer> l = new ArrayList<Integer>();

        for (int i = 0; i < 5000; i++) {
            int sum = 0;
            for (int j = 0; j < i * i; j++)
                sum += j;
            l.add(sum);

            pb.step();
            if (pb.getCurrent() > 1000 && pb.getCurrent() < 4000) pb.setExtraMessage("AAAAAAAAAAAAAAAAAAAAHHHHHHHHH!");
            if (pb.getCurrent() > 4000) pb.setExtraMessage("OH");

        }
        pb.stop();
        System.out.println("Hello");
    }

}
