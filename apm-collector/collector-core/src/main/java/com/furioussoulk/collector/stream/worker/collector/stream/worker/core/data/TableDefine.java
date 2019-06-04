package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.data;

import java.util.LinkedList;
import java.util.List;

public abstract class TableDefine {
    private final String name;
    private final List<ColumnDefine> columnDefines;

    public TableDefine(String name) {
        this.name = name;
        this.columnDefines = new LinkedList<>();
    }

    public abstract void initialize();

    public final void addColumn(ColumnDefine columnDefine) {
        columnDefines.add(columnDefine);
    }

    public final String getName() {
        return name;
    }

    public final List<ColumnDefine> getColumnDefines() {
        return columnDefines;
    }
}
