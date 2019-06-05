package com.furioussoulk.agent.stream;

public enum IdAutoIncrement {
    /**
     * instance
     */
    INSTANCE;

    public int increment(int min, int max) {
        int instanceId;
        if (min == max) {
            instanceId = -1;
        } else if (min + max == 0) {
            instanceId = max + 1;
        } else if (min + max > 0) {
            instanceId = min - 1;
        } else if (max < 0) {
            instanceId = 1;
        } else {
            instanceId = max + 1;
        }
        return instanceId;
    }
}
