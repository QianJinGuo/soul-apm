package com.furioussoulk.apm.core.context.tag;

import com.furioussoulk.apm.core.context.trace.AbstractSpan;

public abstract class AbstractTag<T> {
    /**
     * The key of this Tag.
     */
    protected final String key;

    public AbstractTag(String tagKey) {
        this.key = tagKey;
    }

    protected abstract void set(AbstractSpan span, T tagValue);

    /**
     * @return the key of this tag.
     */
    public String key() {
        return this.key;
    }
}
