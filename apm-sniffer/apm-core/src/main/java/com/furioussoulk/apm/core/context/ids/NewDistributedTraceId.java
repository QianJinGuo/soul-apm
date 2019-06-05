package com.furioussoulk.apm.core.context.ids;

/**
 * The <code>NewDistributedTraceId</code> is a {@link DistributedTraceId} with a new generated id.
 *
 */
public class NewDistributedTraceId extends DistributedTraceId {
    public NewDistributedTraceId() {
        super(GlobalIdGenerator.generate());
    }
}
