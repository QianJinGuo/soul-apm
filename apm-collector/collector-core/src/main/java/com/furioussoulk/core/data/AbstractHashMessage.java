package com.furioussoulk.core.data;

/**
 * The <code>AbstractHashMessage</code> implementations represent aggregate message,
 * which use to aggregate metric.
 * <p>
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/4 11:25
 */
public abstract class AbstractHashMessage {
    private int hashCode;

    public AbstractHashMessage(String key) {
        this.hashCode = key.hashCode();
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setKey(String key) {
        this.hashCode = key.hashCode();
    }
}
