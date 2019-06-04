package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.boot;

public interface BootService {
    void beforeBoot() throws Throwable;

    void boot() throws Throwable;

    void afterBoot() throws Throwable;

    void shutdown() throws Throwable;
}
