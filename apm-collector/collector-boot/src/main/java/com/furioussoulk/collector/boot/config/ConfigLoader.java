package com.furioussoulk.collector.boot.config;

public interface ConfigLoader<T> {
    T load() throws ConfigFileNotFoundException;
}
