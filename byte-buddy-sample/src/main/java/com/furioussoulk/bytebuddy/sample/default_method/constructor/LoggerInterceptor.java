package com.furioussoulk.bytebuddy.sample.default_method.constructor;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/3 15:17
 */
public class LoggerInterceptor {

    @RuntimeType
    public void intercept(@This Object obj,
                          @AllArguments Object[] allArguments) {
        System.out.println("intercept constructor");
    }
}
