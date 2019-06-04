package com.furioussoulk.apm.collector.client.elasticsearch;

import com.furioussoulk.apm.collector.client.ClientException;

public class ElasticSearchClientException extends ClientException {
    public ElasticSearchClientException(String message) {
        super(message);
    }

    public ElasticSearchClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
