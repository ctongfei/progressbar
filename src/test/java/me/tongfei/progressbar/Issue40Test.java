package me.tongfei.progressbar;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 * @author Tongfei Chen
 */
public class Issue40Test {

    @Test
    public void test() throws InterruptedException {
        InputStream input = new ByteArrayInputStream("100 200 300".getBytes());

        Scanner sc = new Scanner(input);
        int x = sc.nextInt();
        try (ProgressBar pb = new ProgressBar("1", x)) {
            for (int i = 0; i < x; i++) {
                Thread.sleep(10);
                pb.step();
            }
        }
        int y = sc.nextInt();
        try (ProgressBar pb = new ProgressBar("2", y)) {
            for (int i = 0; i < y; i++) {
                Thread.sleep(10);
                pb.step();
            }
        }
        int z = sc.nextInt();
        try (ProgressBar pb = new ProgressBar("3", z)) {
            for (int i = 0; i < z; i++) {
                Thread.sleep(10);
                pb.step();
            }
        }
    }

}
