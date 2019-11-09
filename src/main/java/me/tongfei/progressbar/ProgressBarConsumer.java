package me.tongfei.progressbar;

public interface ProgressBarConsumer {

    void beforeUpdate();

    int getMaxSuffixLength(int prefixLength);

    int getMaxProgressLength();

    void accept(String progressBar);

    void close();
}
