package com.furioussoulk.default_method;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/3 16:58
 */
@Retention(RetentionPolicy.RUNTIME)
@interface RuntimeDefinition { }
class RuntimeDefinitionImpl implements RuntimeDefinition {
    @Override
    public Class<? extends Annotation> annotationType() {
        return RuntimeDefinition.class;
    }
}
