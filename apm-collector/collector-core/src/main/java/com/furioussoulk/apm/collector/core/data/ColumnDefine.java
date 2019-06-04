package com.furioussoulk.apm.collector.core.data;

public abstract class ColumnDefine {
    private final String name;
    private final String type;

    public ColumnDefine(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public final String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
