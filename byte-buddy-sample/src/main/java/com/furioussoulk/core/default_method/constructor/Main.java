package com.furioussoulk.core.default_method.constructor;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;

import static net.bytebuddy.matcher.ElementMatchers.any;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/3 17:58
 */
public class Main {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {


        new ByteBuddy(ClassFileVersion.JAVA_V8)
                .subclass(Source.class)
                .constructor(any())
                .intercept(SuperMethodCall.INSTANCE
                        .andThen(MethodDelegation.withDefaultConfiguration()
                                .to(new LoggerInterceptor())
                        )
                )
                .make()
                .load(Main.class.getClassLoader())
                .getLoaded()
                .newInstance();
    }
}
