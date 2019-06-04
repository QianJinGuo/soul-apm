package com.furioussoulk.apm.collector.core.agent.delegate.p4;

import com.furioussoulk.apm.collector.core.agent.delegate.p2.MemoryDatabase;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Pipe;

import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 *
 * @author 孙证杰
 * @date 2019/6/3 15:03
 */
public class Main {

    public static void main(String[] args) throws Exception {

        MemoryDatabase loggingDatabase = new ByteBuddy()
                .subclass(MemoryDatabase.class)
                .method(named("load")).intercept(MethodDelegation.withDefaultConfiguration()
                        .withBinders(Pipe.Binder.install(Forwarder.class))
                .to(new ForwardingLoggerInterceptor(new MemoryDatabase())))
                .make()
                .load(Main.class.getClassLoader())
                .getLoaded()
                .newInstance();

        List<String> hello_world = loggingDatabase.load("hello world123");

        System.out.println(hello_world);
    }
}
