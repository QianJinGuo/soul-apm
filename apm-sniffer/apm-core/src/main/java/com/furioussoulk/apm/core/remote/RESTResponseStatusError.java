
package com.furioussoulk.apm.core.remote;

/**
 * The <code>RESTResponseStatusError</code> represents the REST-Service discovery got an unexpected response code.
 * Most likely, the response code is not 200.
 *
 */
class RESTResponseStatusError extends Exception {
    RESTResponseStatusError(int responseCode) {
        super("Unexpected service response code: " + responseCode);
    }
}
