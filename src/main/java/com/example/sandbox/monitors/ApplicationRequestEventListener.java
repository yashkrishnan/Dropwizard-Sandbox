package com.example.sandbox.monitors;

import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApplicationRequestEventListener implements RequestEventListener {
    private final int requestNumber;
    private final long startTime;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public ApplicationRequestEventListener(int requestNumber) {
        this.requestNumber = requestNumber;
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onEvent(RequestEvent event) {
        switch (event.getType()) {
            case RESOURCE_METHOD_START:
                logger.info("Resource method "
                        + event.getUriInfo().getMatchedResourceMethod()
                        .getHttpMethod()
                        + " started for request " + requestNumber);
                break;
            case FINISHED:
                logger.info("Request " + requestNumber
                        + " finished. Processing time "
                        + (System.currentTimeMillis() - startTime) + " ms.");
                break;
        }
    }
}
