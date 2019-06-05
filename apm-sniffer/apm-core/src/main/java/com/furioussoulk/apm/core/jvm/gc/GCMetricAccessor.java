package com.furioussoulk.apm.core.jvm.gc;

import com.furioussoulk.network.proto.GC;

import java.util.List;

public interface GCMetricAccessor {
    List<GC> getGCList();
}
