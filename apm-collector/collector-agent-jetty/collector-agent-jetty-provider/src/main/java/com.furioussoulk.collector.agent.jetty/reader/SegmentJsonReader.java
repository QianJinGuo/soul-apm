package com.furioussoulk.collector.agent.jetty.reader;

import com.furioussoulk.network.proto.TraceSegmentObject;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SegmentJsonReader implements StreamJsonReader<TraceSegmentObject.Builder> {

    private final Logger logger = LoggerFactory.getLogger(SegmentJsonReader.class);

    private UniqueIdJsonReader uniqueIdJsonReader = new UniqueIdJsonReader();
    private ReferenceJsonReader referenceJsonReader = new ReferenceJsonReader();
    private SpanJsonReader spanJsonReader = new SpanJsonReader();

    private static final String TRACE_SEGMENT_ID = "ts";
    private static final String APPLICATION_ID = "ai";
    private static final String APPLICATION_INSTANCE_ID = "ii";
    private static final String TRACE_SEGMENT_REFERENCE = "rs";
    private static final String SPANS = "ss";

    @Override public TraceSegmentObject.Builder read(JsonReader reader) throws IOException {
        TraceSegmentObject.Builder builder = TraceSegmentObject.newBuilder();

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case TRACE_SEGMENT_ID:
                    builder.setTraceSegmentId(uniqueIdJsonReader.read(reader));
                    if (logger.isDebugEnabled()) {
                        StringBuilder segmentId = new StringBuilder();
                        builder.getTraceSegmentId().getIdPartsList().forEach(idPart -> segmentId.append(idPart));
                        logger.debug("segment id: {}", segmentId);
                    }
                    break;
                case APPLICATION_ID:
                    builder.setApplicationId(reader.nextInt());
                    break;
                case APPLICATION_INSTANCE_ID:
                    builder.setApplicationInstanceId(reader.nextInt());
                    break;
                case TRACE_SEGMENT_REFERENCE:
                    reader.beginArray();
                    while (reader.hasNext()) {
                        builder.addRefs(referenceJsonReader.read(reader));
                    }
                    reader.endArray();
                    break;
                case SPANS:
                    reader.beginArray();
                    while (reader.hasNext()) {
                        builder.addSpans(spanJsonReader.read(reader));
                    }
                    reader.endArray();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return builder;
    }
}
