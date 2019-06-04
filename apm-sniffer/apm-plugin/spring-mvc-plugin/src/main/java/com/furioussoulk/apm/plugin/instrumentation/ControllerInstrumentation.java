package com.furioussoulk.apm.plugin.instrumentation;

public class ControllerInstrumentation extends AbstractControllerInstrumentation {

    public static final String ENHANCE_ANNOTATION = "org.springframework.stereotype.Controller";

    @Override
    protected String[] getEnhanceAnnotations() {
        return new String[]{ENHANCE_ANNOTATION};
    }
}
