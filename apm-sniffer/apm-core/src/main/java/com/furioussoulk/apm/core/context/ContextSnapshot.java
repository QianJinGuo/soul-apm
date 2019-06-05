package com.furioussoulk.apm.core.context;

import com.furioussoulk.apm.core.context.ids.ID;

import java.util.List;

/**
 * The <code>ContextSnapshot</code> is a snapshot for current context. The snapshot carries the info for building
 * reference between two segments in two thread, but have a causal relationship.
 *
 */
public class ContextSnapshot {
    /**
     * trace segment id of the parent trace segment.
     */
    private ID traceSegmentId;

    /**
     * span id of the parent span, in parent trace segment.
     */
    private int spanId = -1;

    private String entryOperationName;

    private String parentOperationName;

    /**
     * {@link DistributedTraceId}
     */
    private DistributedTraceId primaryDistributedTraceId;

    private int entryApplicationInstanceId = DictionaryUtil.nullValue();

    ContextSnapshot(ID traceSegmentId, int spanId,
                    List<DistributedTraceId> distributedTraceIds) {
        this.traceSegmentId = traceSegmentId;
        this.spanId = spanId;
        if (distributedTraceIds != null) {
            this.primaryDistributedTraceId = distributedTraceIds.get(0);
        }
    }

    public void setEntryOperationName(String entryOperationName) {
        this.entryOperationName = "#" + entryOperationName;
    }

    public void setEntryOperationId(int entryOperationId) {
        this.entryOperationName = entryOperationId + "";
    }

    public void setParentOperationName(String parentOperationName) {
        this.parentOperationName = "#" + parentOperationName;
    }

    public void setParentOperationId(int parentOperationId) {
        this.parentOperationName = parentOperationId + "";
    }

    public DistributedTraceId getDistributedTraceId() {
        return primaryDistributedTraceId;
    }

    public ID getTraceSegmentId() {
        return traceSegmentId;
    }

    public int getSpanId() {
        return spanId;
    }

    public String getParentOperationName() {
        return parentOperationName;
    }

    public boolean isValid() {
        return traceSegmentId != null
            && spanId > -1
            && entryApplicationInstanceId != DictionaryUtil.nullValue()
            && primaryDistributedTraceId != null
            && !StringUtil.isEmpty(entryOperationName)
            && !StringUtil.isEmpty(parentOperationName);
    }

    public String getEntryOperationName() {
        return entryOperationName;
    }

    public void setEntryApplicationInstanceId(int entryApplicationInstanceId) {
        this.entryApplicationInstanceId = entryApplicationInstanceId;
    }

    public int getEntryApplicationInstanceId() {
        return entryApplicationInstanceId;
    }

    public boolean isFromCurrent() {
        return traceSegmentId.equals(ContextManager.capture().getTraceSegmentId());
    }
}
