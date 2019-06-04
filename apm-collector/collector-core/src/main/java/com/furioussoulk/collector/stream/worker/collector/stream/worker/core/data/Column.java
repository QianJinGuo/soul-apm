package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.data;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.data.operator.Operation;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/4 11:28
 */
public class Column {

    private final String name;
    private final Operation operation;

    public Column(String name, Operation operation) {
        this.name = name;
        this.operation = operation;
    }

    public String getName() {
        return name;
    }

    public Operation getOperation() {
        return operation;
    }
}