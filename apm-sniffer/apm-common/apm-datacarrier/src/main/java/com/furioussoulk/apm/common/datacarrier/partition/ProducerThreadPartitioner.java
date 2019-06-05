package com.furioussoulk.apm.common.datacarrier.partition;

/**
 * use threadid % total to partition
 *
 */
public class ProducerThreadPartitioner<T> implements IDataPartitioner<T> {

    private int retryTime = 3;

    public ProducerThreadPartitioner() {
    }

    public ProducerThreadPartitioner(int retryTime) {
        this.retryTime = retryTime;
    }

    @Override
    public int partition(int total, T data) {
        return (int)Thread.currentThread().getId() % total;
    }

    @Override
    public int maxRetryCount() {
        return 1;
    }
}
