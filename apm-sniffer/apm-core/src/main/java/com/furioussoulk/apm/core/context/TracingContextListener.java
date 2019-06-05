package com.furioussoulk.apm.core.context;


import com.furioussoulk.apm.core.context.trace.TraceSegment;

public interface TracingContextListener {
    void afterFinished(TraceSegment traceSegment);
}
