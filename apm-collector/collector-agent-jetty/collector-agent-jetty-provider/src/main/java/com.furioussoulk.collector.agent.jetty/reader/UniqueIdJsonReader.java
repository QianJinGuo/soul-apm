package com.furioussoulk.collector.agent.jetty.reader;

import com.furioussoulk.network.proto.UniqueId;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

public class UniqueIdJsonReader implements StreamJsonReader<UniqueId.Builder> {

    @Override public UniqueId.Builder read(JsonReader reader) throws IOException {
        UniqueId.Builder builder = UniqueId.newBuilder();

        reader.beginArray();
        while (reader.hasNext()) {
            builder.addIdParts(reader.nextLong());
        }
        reader.endArray();
        return builder;
    }
}
