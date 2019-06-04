package com.furioussoulk.collector.stream.worker.worker.impl.data;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.cache.Window;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.data.Data;

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
