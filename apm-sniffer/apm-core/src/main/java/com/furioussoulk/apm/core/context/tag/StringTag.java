package com.furioussoulk.apm.core.context.tag;

import com.furioussoulk.apm.core.context.trace.AbstractSpan;

/**
 * A subclass of {@link AbstractTag},
 * represent a tag with a {@link String} value.
 * <p>
 */
public class StringTag extends AbstractTag<String> {
    public StringTag(String tagKey) {
        super(tagKey);
    }

    @Override
    public void set(AbstractSpan span, String tagValue) {
        span.tag(key, tagValue);
    }
}
