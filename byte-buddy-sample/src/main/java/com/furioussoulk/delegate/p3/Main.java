package com.furioussoulk.delegate.p3;

import com.furioussoulk.delegate.p2.LoggerInterceptor;
import com.furioussoulk.delegate.p2.MemoryDatabase;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;

import java.util.List;

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
