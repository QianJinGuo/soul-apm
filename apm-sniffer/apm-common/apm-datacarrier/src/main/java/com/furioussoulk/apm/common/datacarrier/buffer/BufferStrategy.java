package com.furioussoulk.apm.common.datacarrier.buffer;

/**
 * strategy saving buffer
 */
public enum BufferStrategy {

    /**
     * block while buffer[i] is not null
     */
    BLOCKING,

    /**
     * override buffer[i]
     */

    OVERRIDE,

    /**
     * retry while buffer[i] is not null
     */
    IF_POSSIBLE
}
