package com.furioussoulk.client.elasticsearch;

import com.furioussoulk.client.ClientException;

public class ElasticSearchClientException extends ClientException {
    public ElasticSearchClientException(String message) {
        super(message);
    }

    public ElasticSearchClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
