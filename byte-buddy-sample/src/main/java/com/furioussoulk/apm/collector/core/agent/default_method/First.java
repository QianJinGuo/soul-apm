package com.furioussoulk.apm.collector.core.agent.default_method;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/3 16:25
 */
public interface First {
    default String qux() { return "FOO"; }
}
