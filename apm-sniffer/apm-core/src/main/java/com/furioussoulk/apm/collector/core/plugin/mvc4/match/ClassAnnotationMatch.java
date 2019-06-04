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

package com.furioussoulk.apm.collector.core.plugin.mvc4.match;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * Match the class by the given annotations in class.
 *
 * @author wusheng
 */
public class ClassAnnotationMatch implements IndirectMatch {
    private String[] annotations;

    private ClassAnnotationMatch(String[] annotations) {
        if (annotations == null || annotations.length == 0) {
            throw new IllegalArgumentException("annotations is null");
        }
        this.annotations = annotations;
    }

    public static ClassMatch byClassAnnotationMatch(String[] annotations) {
        return new ClassAnnotationMatch(annotations);
    }

    @Override
    public ElementMatcher.Junction buildJunction() {
        ElementMatcher.Junction junction = null;
        for (String annotation : annotations) {
            if (junction == null) {
                junction = buildEachAnnotation(annotation);
            } else {
                junction = junction.and(buildEachAnnotation(annotation));
            }
        }
        junction = junction.and(not(isInterface()));
        return junction;
    }

    @Override
    public boolean isMatch(TypeDescription typeDescription) {
        return true;
   /*     List<String> annotationList = new ArrayList<String>(Arrays.asList(annotations));
        AnnotationList declaredAnnotations = typeDescription.getDeclaredAnnotations();
        for (AnnotationDescription annotation : declaredAnnotations) {
            annotationList.remove(annotation.getAnnotationType().getActualName());
        }
        if (annotationList.isEmpty()) {
            return true;
        }
        return false;*/
    }

    private ElementMatcher.Junction buildEachAnnotation(String annotationName) {
        return isAnnotatedWith(named(annotationName));
    }
}