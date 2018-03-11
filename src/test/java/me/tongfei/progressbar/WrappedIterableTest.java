package me.tongfei.progressbar;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Tongfei Chen
 */
public class WrappedIterableTest {

    static public void main(String[] args) throws Exception {
        System.out.println(System.getenv("TERM"));

        List<Integer> sizedColl = Stream.iterate(1, x -> x + 1).limit(10000).collect(Collectors.toList());

        for (Integer x : ProgressBar.wrap(sizedColl, "Traverse")) {
            Thread.sleep(2);
        }
    }

}
