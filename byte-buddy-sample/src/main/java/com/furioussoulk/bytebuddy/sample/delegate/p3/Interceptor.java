package com.furioussoulk.bytebuddy.sample.delegate.p3;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;

/**
 * @RuntimeType 在参数上表示这是source类方法的返回值，在方法上表示该返回值是source的返回值，如果类型不同会报错。
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/3 15:23
 */
public class Interceptor {
    @RuntimeType
    public static Object intercept(@RuntimeType Object value) {
        System.out.println("Invoked method with: " + value);
        return value;
    }
}
