package com.furioussoulk.collector.agent.jetty.reader;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

public interface StreamJsonReader<T> {
    T read(JsonReader reader) throws IOException;
}
