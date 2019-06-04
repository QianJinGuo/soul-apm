
package com.furioussoulk.apm.core.exception;

public class ProviderNotFoundException extends Exception {
    public ProviderNotFoundException(String message) {
        super(message);
    }

    public ProviderNotFoundException(Throwable e) {
        super(e);
    }
}
