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

package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.plugin.instrumentation;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.interceptor.ConstructorInterceptPoint;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.interceptor.InstanceMethodsInterceptPoint;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.plugin.match.ClassMatch;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.plugin.match.NameMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static com.furioussoulk.collector.stream.worker.collector.stream.worker.core.plugin.constant.Constants.INVOKE_FOR_REQUEST_INTERCEPTOR;
import static net.bytebuddy.matcher.ElementMatchers.named;


/**
 * {@link InvocableHandlerInstrumentation} intercept the <code>invokeForRequest</code> method in the
 * <code>org.springframework.web.method.support.InvocableHandlerMethod</code> class.
 *
 * @author zhangxin
 */
public class InvocableHandlerInstrumentation extends AbstractSpring4Instrumentation {

    public static final String ENHANCE_METHOD = "invokeForRequest";
    public static final String ENHANCE_CLASS = "org.springframework.web.method.support.InvocableHandlerMethod";

    @Override
    protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override
    protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return named(ENHANCE_METHOD);
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return INVOKE_FOR_REQUEST_INTERCEPTOR;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                }
        };
    }

    @Override
    protected ClassMatch enhanceClass() {
        return NameMatch.byName(ENHANCE_CLASS);
    }
}