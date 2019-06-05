package com.furioussoulk.collector.agent.jetty.reader;

import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TraceSegmentJsonReader implements StreamJsonReader<TraceSegment> {

    private final Logger logger = LoggerFactory.getLogger(TraceSegmentJsonReader.class);

    private UniqueIdJsonReader uniqueIdJsonReader = new UniqueIdJsonReader();
    private SegmentJsonReader segmentJsonReader = new SegmentJsonReader();

    private static final String GLOBAL_TRACE_IDS = "gt";
    private static final String SEGMENT = "sg";

    @Override public TraceSegment read(JsonReader reader) throws IOException {
        TraceSegment traceSegment = new TraceSegment();

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case GLOBAL_TRACE_IDS:
                    reader.beginArray();
                    while (reader.hasNext()) {
                        traceSegment.addGlobalTraceId(uniqueIdJsonReader.read(reader));
                    }
                    reader.endArray();

                    break;
                case SEGMENT:
                    traceSegment.setTraceSegmentBuilder(segmentJsonReader.read(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return traceSegment;
    }
}
