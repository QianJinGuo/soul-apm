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

package com.furioussoulk.plugin.bytebuddy;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterList;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * Argument Type match.
 * Similar with {@link net.bytebuddy.matcher.ElementMatchers#takesArgument},
 * the only different between them is this match use {@link String} to declare the type, instead of {@link Class}.
 * This can avoid the classloader risk.
 * <p>
 * Created by wusheng on 2016/12/1.
 */
public class ArgumentTypeNameMatch implements ElementMatcher<MethodDescription> {
    /**
     * the index of arguments list.
     */
    private int index;

    /**
     * the target argument type at {@link ArgumentTypeNameMatch#index} of the arguments list.
     */
    private String argumentTypeName;

    /**
     * declare the match target method with the certain index and type.
     *
     * @param index            the index of arguments list.
     * @param argumentTypeName target argument type
     */
    private ArgumentTypeNameMatch(int index, String argumentTypeName) {
        this.index = index;
        this.argumentTypeName = argumentTypeName;
    }

    /**
     * The static method to create {@link ArgumentTypeNameMatch}
     * This is a delegate method to follow byte-buddy {@link ElementMatcher<MethodDescription>}'s code style.
     *
     * @param index            the index of arguments list.
     * @param argumentTypeName target argument type
     * @return new {@link ArgumentTypeNameMatch} instance.
     */
    public static ElementMatcher<MethodDescription> takesArgumentWithType(int index, String argumentTypeName) {
        return new ArgumentTypeNameMatch(index, argumentTypeName);
    }

    /**
     * Match the target method.
     *
     * @param target target method description.
     * @return true if matched. or false.
     */
    @Override
    public boolean matches(MethodDescription target) {
        ParameterList<?> parameters = target.getParameters();
        if (parameters.size() > index) {
            return parameters.get(index).getType().asErasure().getName().equals(argumentTypeName);
        }

        return false;
    }
}
