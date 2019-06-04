package com.furioussoulk.apm.collector.core.agent.delegate.p2;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author 孙证杰
 * @date 2019/6/3 15:15
 */
public class MemoryDatabase {
    public List<String> load(String info) throws Exception {
        return Arrays.asList(info + ": foo", info + ": bar");
    }
}
