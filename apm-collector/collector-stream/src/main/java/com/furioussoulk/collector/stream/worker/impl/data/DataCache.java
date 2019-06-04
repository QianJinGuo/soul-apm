package com.furioussoulk.collector.stream.worker.impl.data;

import com.furioussoulk.apm.collector.core.cache.Window;
import com.furioussoulk.apm.collector.core.data.Data;

public class DataCache extends Window<DataCollection> {

    private DataCollection lockedDataCollection;

    @Override public DataCollection collectionInstance() {
        return new DataCollection();
    }

    public boolean containsKey(String id) {
        return lockedDataCollection.containsKey(id);
    }

    public Data get(String id) {
        return lockedDataCollection.get(id);
    }

    public void put(String id, Data data) {
        lockedDataCollection.put(id, data);
    }

    public void writing() {
        lockedDataCollection = getCurrentAndWriting();
    }

    public int currentCollectionSize() {
        return getCurrent().size();
    }

    public void finishWriting() {
        lockedDataCollection.finishWriting();
        lockedDataCollection = null;
    }
}
