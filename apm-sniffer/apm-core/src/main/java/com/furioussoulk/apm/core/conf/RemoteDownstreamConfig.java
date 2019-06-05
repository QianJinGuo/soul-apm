package com.furioussoulk.apm.core.conf;

import com.furioussoulk.apm.core.dictionary.DictionaryUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * The <code>RemoteDownstreamConfig</code> includes configurations from collector side.
 * All of them initialized null, Null-Value or empty collection.
 *
 */
public class RemoteDownstreamConfig {
    public static class Agent {
        public volatile static int APPLICATION_ID = DictionaryUtil.nullValue();

        public volatile static int APPLICATION_INSTANCE_ID = DictionaryUtil.nullValue();
    }

    public static class Collector {
        /**
         * Collector GRPC-Service address.
         */
        public volatile static List<String> GRPC_SERVERS = new LinkedList<String>();
    }
}
