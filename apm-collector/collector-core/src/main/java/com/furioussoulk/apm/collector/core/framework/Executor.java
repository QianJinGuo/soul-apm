package com.furioussoulk.apm.collector.core.framework;

import com.furioussoulk.apm.core.exception.CollectorException;

/**
 * @author 孙证杰
 * @date 2019/6/3 13:23
 */
public interface Executor<INPUT> {
    void execute(INPUT input) throws CollectorException;
}
