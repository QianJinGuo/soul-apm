package com.furioussoulk.collector.agent.jetty.reader;

import com.furioussoulk.network.proto.KeyWithStringValue;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

public class KeyWithStringValueJsonReader implements StreamJsonReader<KeyWithStringValue> {

    private static final String KEY = "k";
    private static final String VALUE = "v";

    @Override public KeyWithStringValue read(JsonReader reader) throws IOException {
        KeyWithStringValue.Builder builder = KeyWithStringValue.newBuilder();

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case KEY:
                    builder.setKey(reader.nextString());
                    break;
                case VALUE:
                    builder.setValue(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return builder.build();
    }
}
