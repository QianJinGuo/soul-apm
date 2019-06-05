

package com.furioussoulk.apm.core.interceptor.enhance;


import com.furioussoulk.apm.core.interceptor.ConstructorInterceptPoint;
import com.furioussoulk.apm.core.interceptor.InstanceMethodsInterceptPoint;

/**
 * Plugins, which only need enhance class static methods. Actually, inherit from {@link
 * ClassStaticMethodsEnhancePluginDefine} has no differences with inherit from {@link ClassEnhancePluginDefine}. Just
 * override {@link ClassEnhancePluginDefine#getConstructorsInterceptPoints} and {@link
 * ClassEnhancePluginDefine#getInstanceMethodsInterceptPoints}, and return {@link null}, which means nothing to
 * enhance.
 */
public abstract class ClassStaticMethodsEnhancePluginDefine extends ClassEnhancePluginDefine {

    /**
     * @return null, means enhance no constructors.
     */
    @Override
    protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return null;
    }

    /**
     * @return null, means enhance no instance methods.
     */
    @Override
    protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return null;
    }
}
