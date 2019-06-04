package com.furioussoulk.core.data.operator;

public interface Operation {
    String operate(String newValue, String oldValue);

    Long operate(Long newValue, Long oldValue);

    Double operate(Double newValue, Double oldValue);

    Integer operate(Integer newValue, Integer oldValue);

    Boolean operate(Boolean newValue, Boolean oldValue);

    byte[] operate(byte[] newValue, byte[] oldValue);
}
