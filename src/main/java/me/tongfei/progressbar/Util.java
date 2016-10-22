package me.tongfei.progressbar;

import java.time.Duration;

/**
 * @author Tongfei Chen
 * @since 0.5.0
 */
public class Util {

    static String repeat(char c, int n) {
        if (n <= 0) return "";
        char[] s = new char[n];
        for (int i = 0; i < n; i++) s[i] = c;
        return new String(s);
    }

    static String formatDuration(Duration d) {
        long s = d.getSeconds();
        return String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60);
    }

}
