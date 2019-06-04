package com.furioussoulk.apm.collector.core.core.logger;

import com.furioussoulk.apm.collector.core.core.logger.api.IWriter;

import java.io.PrintStream;

public enum SystemOutWriter implements IWriter {
    INSTANCE;

    @Override
    public void write(String message) {
        PrintStream out = System.out;
        out.println(message);
    }
}
