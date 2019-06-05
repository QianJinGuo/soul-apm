package com.furioussoulk.collector.agent.jetty;

import com.furioussoulk.apm.collector.server.jetty.JettyHandler;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ServiceNameDiscoveryServiceHandler extends JettyHandler {

    private final Logger logger = LoggerFactory.getLogger(ServiceNameDiscoveryServiceHandler.class);

    private final IServiceNameService serviceNameService;
    private final Gson gson = new Gson();

    private static final String APPLICATION_ID = "ai";
    private static final String SERVICE_NAME = "sn";
    private static final String SERVICE_ID = "si";
    private static final String ELEMENT = "el";

    public ServiceNameDiscoveryServiceHandler(ModuleManager moduleManager) {
        this.serviceNameService = moduleManager.find(AgentStreamModule.NAME).getService(IServiceNameService.class);
    }

    @Override public String pathSpec() {
        return "/servicename/discovery";
    }

    @Override protected JsonElement doGet(HttpServletRequest req) throws ArgumentsParseException {
        throw new UnsupportedOperationException();
    }

    @Override protected JsonElement doPost(HttpServletRequest req) throws ArgumentsParseException {
        JsonArray responseArray = new JsonArray();
        try {
            JsonArray services = gson.fromJson(req.getReader(), JsonArray.class);
            for (JsonElement service : services) {
                int applicationId = service.getAsJsonObject().get(APPLICATION_ID).getAsInt();
                String serviceName = service.getAsJsonObject().get(SERVICE_NAME).getAsString();

                int serviceId = serviceNameService.getOrCreate(applicationId, serviceName);
                if (serviceId != 0) {
                    JsonObject responseJson = new JsonObject();
                    responseJson.addProperty(SERVICE_ID, serviceId);
                    responseJson.add(ELEMENT, service);
                    responseArray.add(responseJson);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return responseArray;
    }
}
