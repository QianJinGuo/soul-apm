package com.furioussoulk.collector.stream.worker.impl.data;

import com.furioussoulk.apm.collector.core.cache.Collection;
import com.furioussoulk.apm.collector.core.data.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataCollection implements Collection<Map<String, Data>> {
    private Map<String, Data> data;
    private volatile boolean writing;
    private volatile boolean reading;

    public DataCollection() {
        this.data = new ConcurrentHashMap<>();
        this.writing = false;
        this.reading = false;
    }

    @Override
    public void finishWriting() {
        writing = false;
    }

    @Override public void writing() {
        writing = true;
    }

    @Override public boolean isWriting() {
        return writing;
    }

    @Override public void finishReading() {
        reading = false;
    }

    @Override public void reading() {
        reading = true;
    }

    @Override public boolean isReading() {
        return reading;
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public void put(String key, Data value) {
        data.put(key, value);
    }

    public Data get(String key) {
        return data.get(key);
    }

    @Override public int size() {
        return data.size();
    }

    @Override public void clear() {
        data.clear();
    }

    public Map<String, Data> collection() {
        return data;
    }
}
