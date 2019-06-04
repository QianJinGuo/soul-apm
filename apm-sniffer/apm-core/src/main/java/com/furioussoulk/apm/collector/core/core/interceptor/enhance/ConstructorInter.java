/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package com.furioussoulk.apm.collector.core.core.interceptor.enhance;

import com.furioussoulk.apm.collector.core.core.logger.LogManager;
import com.furioussoulk.apm.collector.core.core.logger.api.ILogger;
import com.furioussoulk.apm.collector.core.plugin.mvc4.exception.PluginException;
import com.furioussoulk.apm.collector.core.plugin.mvc4.loader.InterceptorInstanceLoader;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;


/**
 * The actual byte-buddy's interceptor to intercept constructor methods.
 * In this class, it provide a bridge between byte-buddy and sky-walking plugin.
 */
public class ConstructorInter {
    private static final ILogger logger = LogManager.getLogger(ConstructorInter.class);

    /**
     * An {@link InstanceConstructorInterceptor}
     * This name should only stay in {@link String}, the real {@link Class} type will trigger classloader failure.
     * If you want to know more, please check on books about Classloader or Classloader appointment mechanism.
     */
    private InstanceConstructorInterceptor interceptor;

    /**
     * @param constructorInterceptorClassName class full name.
     */
    public ConstructorInter(String constructorInterceptorClassName, ClassLoader classLoader) throws PluginException {
        try {
            interceptor = InterceptorInstanceLoader.load(constructorInterceptorClassName, classLoader);
        } catch (Throwable t) {
            throw new PluginException("Can't create InstanceConstructorInterceptor.", t);
        }
    }

    /**
     * Intercept the target constructor.
     *
     * @param obj          target class instance.
     * @param allArguments all constructor arguments
     */
    @RuntimeType
    public void intercept(@This Object obj,
                          @AllArguments Object[] allArguments) {
        try {
            EnhancedInstance targetObject = (EnhancedInstance) obj;

            interceptor.onConstruct(targetObject, allArguments);
        } catch (Throwable t) {
            logger.error("ConstructorInter failure.", t);
        }

    }
}