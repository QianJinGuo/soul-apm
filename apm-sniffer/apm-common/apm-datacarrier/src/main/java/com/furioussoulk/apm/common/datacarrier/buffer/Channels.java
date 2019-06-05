package com.furioussoulk.apm.common.datacarrier.buffer;

import com.furioussoulk.apm.common.datacarrier.partition.IDataPartitioner;

/**
 * Channels of Buffer
 * It contais all buffer data which belongs to this channel.
 * It supports several strategy when buffer is full. The Default is BLOCKING
 * <p>
 */
public class Channels<T> {
    /**
     * Buffer array
     */
    private final Buffer<T>[] bufferChannels;
    /**
     * pick Buffer automatically
     */
    private IDataPartitioner<T> dataPartitioner;
    /**
     * saving Buffer strategy
     */
    private BufferStrategy strategy;

    public Channels(int channelSize, int bufferSize, IDataPartitioner<T> partitioner, BufferStrategy strategy) {
        this.dataPartitioner = partitioner;
        this.strategy = strategy;
        bufferChannels = new Buffer[channelSize];
        for (int i = 0; i < channelSize; i++) {
            bufferChannels[i] = new Buffer<T>(bufferSize, strategy);
        }
    }

    public boolean save(T data) {
        int index = dataPartitioner.partition(bufferChannels.length, data);
        int retryCountDown = 1;
        if (BufferStrategy.IF_POSSIBLE.equals(strategy)) {
            int maxRetryCount = dataPartitioner.maxRetryCount();
            if (maxRetryCount > 1) {
                retryCountDown = maxRetryCount;
            }
        }
        for (; retryCountDown > 0; retryCountDown--) {
            if (bufferChannels[index].save(data)) {
                return true;
            }
        }
        return false;
    }

    public void setPartitioner(IDataPartitioner<T> dataPartitioner) {
        this.dataPartitioner = dataPartitioner;
    }

    /**
     * override the strategy at runtime. Notice, this will override several channels one by one. So, when running
     * setStrategy, each channel may use different BufferStrategy
     *
     * @param strategy
     */
    public void setStrategy(BufferStrategy strategy) {
        for (Buffer<T> buffer : bufferChannels) {
            buffer.setStrategy(strategy);
        }
    }

    /**
     * get channelSize
     *
     * @return
     */
    public int getChannelSize() {
        return this.bufferChannels.length;
    }

    public Buffer<T> getBuffer(int index) {
        return this.bufferChannels[index];
    }
}
