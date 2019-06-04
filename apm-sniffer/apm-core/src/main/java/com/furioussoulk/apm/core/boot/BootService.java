package com.furioussoulk.apm.core.boot;

public interface BootService {
    void beforeBoot() throws Throwable;

    void boot() throws Throwable;

    void afterBoot() throws Throwable;

    void shutdown() throws Throwable;
}
