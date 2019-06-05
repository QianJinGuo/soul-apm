package com.furioussoulk.agent.stream.parser;

import com.furioussoulk.agent.stream.parser.standardization.*;
import com.furioussoulk.agent.stream.worker.trace.global.GlobalTraceSpanListener;
import com.furioussoulk.agent.stream.worker.trace.instance.InstPerformanceSpanListener;
import com.furioussoulk.agent.stream.worker.trace.node.*;
import com.furioussoulk.agent.stream.worker.trace.noderef.NodeReferenceSpanListener;
import com.furioussoulk.agent.stream.worker.trace.segment.SegmentCostSpanListener;
import com.furioussoulk.agent.stream.worker.trace.service.ServiceEntrySpanListener;
import com.furioussoulk.agent.stream.worker.trace.serviceref.ServiceReferenceSpanListener;
import com.furioussoulk.apm.collector.core.graph.Graph;
import com.furioussoulk.apm.collector.core.module.ModuleManager;
import com.furioussoulk.apm.collector.core.util.TimeBucketUtils;
import com.furioussoulk.network.proto.SpanType;
import com.furioussoulk.network.proto.TraceSegmentObject;
import com.furioussoulk.network.proto.UniqueId;
import com.furioussoulk.network.proto.UpstreamSegment;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peng-yongsheng
 */
public class SegmentParse {

    private final Logger logger = LoggerFactory.getLogger(SegmentParse.class);

    private final List<SpanListener> spanListeners;
    private final ModuleManager moduleManager;
    private String segmentId;
    private long timeBucket = 0;

    public SegmentParse(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
        this.spanListeners = new ArrayList<>();
        this.spanListeners.add(new NodeComponentSpanListener());
        this.spanListeners.add(new NodeMappingSpanListener());
        this.spanListeners.add(new NodeReferenceSpanListener(moduleManager));
        this.spanListeners.add(new SegmentCostSpanListener(moduleManager));
        this.spanListeners.add(new GlobalTraceSpanListener());
        this.spanListeners.add(new ServiceEntrySpanListener(moduleManager));
        this.spanListeners.add(new ServiceReferenceSpanListener());
        this.spanListeners.add(new InstPerformanceSpanListener());
    }

    public boolean parse(UpstreamSegment segment, Source source) {
        try {
            List<UniqueId> traceIds = segment.getGlobalTraceIdsList();
            TraceSegmentObject segmentObject = TraceSegmentObject.parseFrom(segment.getSegment());

            SegmentDecorator segmentDecorator = new SegmentDecorator(segmentObject);

            if (!preBuild(traceIds, segmentDecorator)) {
                logger.debug("This segment id exchange not success, write to buffer file, id: {}", segmentId);

                if (source.equals(Source.Agent)) {
                    writeToBufferFile(segmentId, segment);
                }
                return false;
            } else {
                logger.debug("This segment id exchange success, id: {}", segmentId);
                notifyListenerToBuild();
                buildSegment(segmentId, segmentDecorator.toByteArray());
                return true;
            }
        } catch (InvalidProtocolBufferException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    private boolean preBuild(List<UniqueId> traceIds, SegmentDecorator segmentDecorator) {
        StringBuilder segmentIdBuilder = new StringBuilder();

        for (int i = 0; i < segmentDecorator.getTraceSegmentId().getIdPartsList().size(); i++) {
            if (i == 0) {
                segmentIdBuilder.append(segmentDecorator.getTraceSegmentId().getIdPartsList().get(i));
            } else {
                segmentIdBuilder.append(".").append(segmentDecorator.getTraceSegmentId().getIdPartsList().get(i));
            }
        }

        segmentId = segmentIdBuilder.toString();

        for (UniqueId uniqueId : traceIds) {
            notifyGlobalsListener(uniqueId);
        }

        int applicationId = segmentDecorator.getApplicationId();
        int applicationInstanceId = segmentDecorator.getApplicationInstanceId();

        for (int i = 0; i < segmentDecorator.getRefsCount(); i++) {
            ReferenceDecorator referenceDecorator = segmentDecorator.getRefs(i);
            if (!ReferenceIdExchanger.getInstance(moduleManager).exchange(referenceDecorator, applicationId)) {
                return false;
            }

            notifyRefsListener(referenceDecorator, applicationId, applicationInstanceId, segmentId);
        }

        for (int i = 0; i < segmentDecorator.getSpansCount(); i++) {
            SpanDecorator spanDecorator = segmentDecorator.getSpans(i);

            if (!SpanIdExchanger.getInstance(moduleManager).exchange(spanDecorator, applicationId)) {
                return false;
            }

            if (spanDecorator.getSpanId() == 0) {
                notifyFirstListener(spanDecorator, applicationId, applicationInstanceId, segmentId);
                timeBucket = TimeBucketUtils.INSTANCE.getMinuteTimeBucket(spanDecorator.getStartTime());
            }

            if (SpanType.Exit.equals(spanDecorator.getSpanType())) {
                notifyExitListener(spanDecorator, applicationId, applicationInstanceId, segmentId);
            } else if (SpanType.Entry.equals(spanDecorator.getSpanType())) {
                notifyEntryListener(spanDecorator, applicationId, applicationInstanceId, segmentId);
            } else if (SpanType.Local.equals(spanDecorator.getSpanType())) {
                notifyLocalListener(spanDecorator, applicationId, applicationInstanceId, segmentId);
            } else {
                logger.error("span type value was unexpected, span type name: {}", spanDecorator.getSpanType().name());
            }
        }

        return true;
    }

    private void buildSegment(String id, byte[] dataBinary) {
        Segment segment = new Segment(id);
        segment.setDataBinary(dataBinary);
        segment.setTimeBucket(timeBucket);
        Graph<Segment> graph = GraphManager.INSTANCE.createIfAbsent(TraceStreamGraph.SEGMENT_GRAPH_ID, Segment.class);
        graph.start(segment);
    }

    private void writeToBufferFile(String id, UpstreamSegment upstreamSegment) {
        logger.debug("push to segment buffer write worker, id: {}", id);
        SegmentStandardization standardization = new SegmentStandardization(id);
        standardization.setUpstreamSegment(upstreamSegment);
        Graph<SegmentStandardization> graph = GraphManager.INSTANCE.createIfAbsent(TraceStreamGraph.SEGMENT_STANDARDIZATION_GRAPH_ID, SegmentStandardization.class);
        graph.start(standardization);
    }

    private void notifyListenerToBuild() {
        spanListeners.forEach(SpanListener::build);
    }

    private void notifyExitListener(SpanDecorator spanDecorator, int applicationId, int applicationInstanceId,
        String segmentId) {
        for (SpanListener listener : spanListeners) {
            if (listener instanceof ExitSpanListener) {
                ((ExitSpanListener)listener).parseExit(spanDecorator, applicationId, applicationInstanceId, segmentId);
            }
        }
    }

    private void notifyEntryListener(SpanDecorator spanDecorator, int applicationId, int applicationInstanceId,
        String segmentId) {
        for (SpanListener listener : spanListeners) {
            if (listener instanceof EntrySpanListener) {
                ((EntrySpanListener)listener).parseEntry(spanDecorator, applicationId, applicationInstanceId, segmentId);
            }
        }
    }

    private void notifyLocalListener(SpanDecorator spanDecorator, int applicationId, int applicationInstanceId,
        String segmentId) {
        for (SpanListener listener : spanListeners) {
            if (listener instanceof LocalSpanListener) {
                ((LocalSpanListener)listener).parseLocal(spanDecorator, applicationId, applicationInstanceId, segmentId);
            }
        }
    }

    private void notifyFirstListener(SpanDecorator spanDecorator, int applicationId, int applicationInstanceId,
        String segmentId) {
        for (SpanListener listener : spanListeners) {
            if (listener instanceof FirstSpanListener) {
                ((FirstSpanListener)listener).parseFirst(spanDecorator, applicationId, applicationInstanceId, segmentId);
            }
        }
    }

    private void notifyRefsListener(ReferenceDecorator reference, int applicationId, int applicationInstanceId,
        String segmentId) {
        for (SpanListener listener : spanListeners) {
            if (listener instanceof RefsListener) {
                ((RefsListener)listener).parseRef(reference, applicationId, applicationInstanceId, segmentId);
            }
        }
    }

    private void notifyGlobalsListener(UniqueId uniqueId) {
        for (SpanListener listener : spanListeners) {
            if (listener instanceof GlobalTraceIdsListener) {
                ((GlobalTraceIdsListener)listener).parseGlobalTraceId(uniqueId);
            }
        }
    }

    public enum Source {
        Agent, Buffer
    }
}
