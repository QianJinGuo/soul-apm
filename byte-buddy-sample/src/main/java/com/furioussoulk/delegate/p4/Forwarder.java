package com.furioussoulk.delegate.p4;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/3 15:27
 */
public interface Forwarder<T, S> {
    T to(S target);
}
