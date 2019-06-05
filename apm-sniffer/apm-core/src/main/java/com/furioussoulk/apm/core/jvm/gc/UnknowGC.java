package com.furioussoulk.apm.core.jvm.gc;

import com.furioussoulk.network.proto.GC;
import com.furioussoulk.network.proto.GCPhrase;

import java.util.LinkedList;
import java.util.List;

public class UnknowGC implements GCMetricAccessor {
    @Override
    public List<GC> getGCList() {
        List<GC> gcList = new LinkedList<GC>();
        gcList.add(GC.newBuilder().setPhrase(GCPhrase.NEW).build());
        gcList.add(GC.newBuilder().setPhrase(GCPhrase.OLD).build());
        return gcList;
    }
}
