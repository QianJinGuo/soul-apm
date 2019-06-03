package com.furioussoulk.delegate.p5;

import com.furioussoulk.delegate.p3.Interceptor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;

import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @author 孙证杰
 * @date 2019/6/3 15:03
 */
public class Main {

    public static void main(String[] args) throws Exception {

        LoggingMemoryDatabase ins = new ByteBuddy()
                .subclass(LoggingMemoryDatabase.class)
                .method(named("load")).intercept(MethodDelegation.to(ChangingLoggerInterceptor.class))
                .make()
                .load(Main.class.getClassLoader())
                .getLoaded()
                .newInstance();

        List<String> foofoo = ins.load("foofoo");

        System.out.println(foofoo);
    }
}
