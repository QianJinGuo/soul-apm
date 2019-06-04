package com.furioussoulk.core.data;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/4 11:26
 */
public abstract class EndOfBatchQueueMessage extends AbstractHashMessage{

    private boolean endOfBatch;

    public EndOfBatchQueueMessage(String key) {
        super(key);
        endOfBatch = false;
    }

    public final boolean isEndOfBatch() {
        return endOfBatch;
    }

    public final void setEndOfBatch(boolean endOfBatch) {
        this.endOfBatch = endOfBatch;
    }
}
