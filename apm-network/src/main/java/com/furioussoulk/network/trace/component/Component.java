package com.furioussoulk.network.trace.component;

/**
 * The <code>Component</code> represents component library,
 * which has been supported by skywalking sniffer.
 *
 * The supported list is in {@link ComponentsDefine}.
 *
 */
public interface Component {
    int getId();

    String getName();
}
