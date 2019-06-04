package com.furioussoulk.apm.collector.core.data.operator;

public class NonOperation implements Operation {
    @Override
    public String operate(String newValue, String oldValue) {
        return oldValue;
    }

    @Override
    public Long operate(Long newValue, Long oldValue) {
        return oldValue;
    }

    @Override
    public Double operate(Double newValue, Double oldValue) {
        return oldValue;
    }

    @Override
    public Integer operate(Integer newValue, Integer oldValue) {
        return oldValue;
    }

    @Override
    public Boolean operate(Boolean newValue, Boolean oldValue) {
        return oldValue;
    }

    @Override
    public byte[] operate(byte[] newValue, byte[] oldValue) {
        return oldValue;
    }
}
