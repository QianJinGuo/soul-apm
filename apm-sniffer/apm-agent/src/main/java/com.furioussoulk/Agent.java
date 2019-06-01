package com.furioussoulk;

import com.furioussoulk.boot.ServiceManager;
import com.furioussoulk.logger.LogManager;
import com.furioussoulk.logger.api.ILogger;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;


public class Agent {

    private static final ILogger logger = LogManager.getLogger(Agent.class);

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        ServiceManager.INSTANCE.boot();

        new AgentBuilder.Default().type(ElementMatchers.nameStartsWith("com.furioussoulk.")).transform(new AgentBuilder.Transformer() {

            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {

                return builder
                        .method(ElementMatchers.any()) // 拦截任意方法
                        .intercept(MethodDelegation.to(TimeInterceptor.class)); // 委托
            }

        }).with(new AgentBuilder.Listener() {
            @Override
            public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
                                         boolean loaded, DynamicType dynamicType) {
                if (logger.isDebugEnable()) {
                    logger.debug("On Transformation class {}.", typeDescription.getName());
                }

                InstrumentDebuggingClass.INSTANCE.log(typeDescription, dynamicType);
            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
                                  boolean loaded) {

            }

            @Override
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded,
                                Throwable throwable) {
                logger.error("Enhance class " + typeName + " error.", throwable);
            }

            @Override
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
            }
        }).installOn(instrumentation);
    }
}

final class TimeInterceptor {
    @RuntimeType
    public static Object intercept(@Origin Method method,
                                   @SuperCall Callable<?> callable) throws Exception {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(1000);
            // 原有函数执行
            return callable.call();
        } finally {
            System.out.println(method + ": took " + (System.currentTimeMillis() - start) + "ms");
        }
    }
}