package com.furioussoulk.collector.agent.jetty;

import com.furioussoulk.apm.collector.server.jetty.JettyHandler;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class InstanceDiscoveryServletHandler extends JettyHandler {

    private final Logger logger = LoggerFactory.getLogger(InstanceDiscoveryServletHandler.class);

    private final IInstanceIDService instanceIDService;
    private final Gson gson = new Gson();

    private static final String APPLICATION_ID = "ai";
    private static final String AGENT_UUID = "au";
    private static final String REGISTER_TIME = "rt";
    private static final String INSTANCE_ID = "ii";
    private static final String OS_INFO = "oi";

    public InstanceDiscoveryServletHandler(ModuleManager moduleManager) {
        this.instanceIDService = moduleManager.find(AgentStreamModule.NAME).getService(IInstanceIDService.class);
    }

    @Override public String pathSpec() {
        return "/instance/register";
    }

    @Override protected JsonElement doGet(HttpServletRequest req) throws ArgumentsParseException {
        throw new UnsupportedOperationException();
    }

    @Override protected JsonElement doPost(HttpServletRequest req) throws ArgumentsParseException {
        JsonObject responseJson = new JsonObject();
        try {
            JsonObject instance = gson.fromJson(req.getReader(), JsonObject.class);
            int applicationId = instance.get(APPLICATION_ID).getAsInt();
            String agentUUID = instance.get(AGENT_UUID).getAsString();
            long registerTime = instance.get(REGISTER_TIME).getAsLong();
            JsonObject osInfo = instance.get(OS_INFO).getAsJsonObject();

            int instanceId = instanceIDService.getOrCreate(applicationId, agentUUID, registerTime, osInfo.toString());
            responseJson.addProperty(APPLICATION_ID, applicationId);
            responseJson.addProperty(INSTANCE_ID, instanceId);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return responseJson;
    }
}
