package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.delegate.p3;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 *
 * @author 孙证杰
 * @date 2019/6/3 15:03
 */
public class Main {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        Loop ins = new ByteBuddy()
                .subclass(Loop.class)
                .method(named("loop")).intercept(MethodDelegation.to(Interceptor.class))
                .make()
                .load(Main.class.getClassLoader())
                .getLoaded()
                .newInstance();
        String result = ins.loop("foofoo");

        System.out.println(result);
    }
}
