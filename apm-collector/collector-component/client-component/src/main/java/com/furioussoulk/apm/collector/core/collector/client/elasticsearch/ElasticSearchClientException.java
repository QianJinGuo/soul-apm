package com.furioussoulk.apm.collector.core.collector.client.elasticsearch;

import com.furioussoulk.apm.collector.core.collector.client.ClientException;

public class ElasticSearchClientException extends ClientException {
    public ElasticSearchClientException(String message) {
        super(message);
    }

    public ElasticSearchClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
