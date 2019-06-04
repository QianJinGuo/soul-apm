package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger.api.IWriter;

import java.io.PrintStream;

public enum SystemOutWriter implements IWriter {
    INSTANCE;

    @Override
    public void write(String message) {
        PrintStream out = System.out;
        out.println(message);
    }
}
