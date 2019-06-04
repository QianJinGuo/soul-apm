package com.furioussoulk.apm.collector.core.agent.delegate.p1;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 *
 * @author 孙证杰
 * @date 2019/6/3 15:03
 */
public class Main {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        Source ins = new ByteBuddy()
                .subclass(Source.class)
                .method(named("hello"))
                .intercept(SuperMethodCall.INSTANCE.andThen(MethodDelegation.to(Target.class)))
                .make()
                .load(Main.class.getClassLoader())
                .getLoaded()
                .newInstance();

        System.out.println(ins.hello("World"));
    }
}
