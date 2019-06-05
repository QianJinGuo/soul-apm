

package com.furioussoulk.apm.core.interceptor;

public class InterceptorException extends RuntimeException {
    private static final long serialVersionUID = 7846035239994885019L;

    public InterceptorException(String message) {
        super(message);
    }

    public InterceptorException(String message, Throwable cause) {
        super(message, cause);
    }
}
