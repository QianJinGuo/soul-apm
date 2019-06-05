package com.furioussoulk.apm.common.datacarrier.partition;

public interface IDataPartitioner<T> {
    int partition(int total, T data);

    /**
     * @return an integer represents how many times should retry when {@link BufferStrategy#IF_POSSIBLE}.
     *
     * Less or equal 1, means not support retry.
     */
    int maxRetryCount();
}
