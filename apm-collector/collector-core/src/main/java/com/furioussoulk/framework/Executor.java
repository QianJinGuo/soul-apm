package com.furioussoulk.framework;

import com.furioussoulk.exception.CollectorException;

/**
 * @author 孙证杰
 * @date 2019/6/3 13:23
 */
public interface Executor<INPUT> {
    void execute(INPUT input) throws CollectorException;
}
