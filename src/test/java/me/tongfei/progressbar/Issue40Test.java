package me.tongfei.progressbar;

import org.junit.Test;

import java.util.Scanner;

/**
 * @author Tongfei Chen
 */
public class Issue40Test {

    @Test
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
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
