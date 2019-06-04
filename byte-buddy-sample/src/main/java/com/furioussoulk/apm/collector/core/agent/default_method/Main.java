package com.furioussoulk.apm.collector.core.agent.default_method;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.implementation.DefaultMethodCall;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @author 孙证杰
 * @date 2019/6/3 15:03
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Object qux = new ByteBuddy(ClassFileVersion.JAVA_V8)
                .subclass(Object.class)
                .implement(First.class)
                .implement(Second.class)
                .method(named("qux")).intercept(DefaultMethodCall.prioritize(Second.class))
                .make()
                .load(com.furioussoulk.apm.collector.core.agent.delegate.p5.Main.class.getClassLoader())
                .getLoaded()
                .newInstance();

        System.out.println(((Second) qux).qux());
    }
}
