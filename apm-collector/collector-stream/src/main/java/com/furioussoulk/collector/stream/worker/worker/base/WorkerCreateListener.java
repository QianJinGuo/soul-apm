package com.furioussoulk.collector.stream.worker.worker.base;

import com.furioussoulk.collector.stream.worker.worker.impl.PersistenceWorker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peng-yongsheng
 */
public class WorkerCreateListener {

    private final List<PersistenceWorker> persistenceWorkers;

    public WorkerCreateListener() {
        this.persistenceWorkers = new ArrayList<>();
    }

    public void addWorker(AbstractWorker worker) {
        if (worker instanceof PersistenceWorker) {
            persistenceWorkers.add((PersistenceWorker)worker);
        }
    }

    public List<PersistenceWorker> getPersistenceWorkers() {
        return persistenceWorkers;
    }
}
