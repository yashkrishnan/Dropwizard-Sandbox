package com.example.sandbox.monitors;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationEventsListener implements ApplicationEventListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private volatile int requestCount = 0;

    @Override
    public void onEvent(ApplicationEvent event) {
        switch (event.getType()) {
            case INITIALIZATION_START:
                logger.info("Application "
                        + event.getResourceConfig().getApplicationName()
                        + " initialization started.");
                break;
            case INITIALIZATION_FINISHED:
                logger.info("Application "
                        + event.getResourceConfig().getApplicationName()
                        + " was finished initialization.");
                break;
            case DESTROY_FINISHED:
                logger.info("Application "
                        + event.getResourceConfig().getApplicationName() + " destroyed.");
                break;
        }
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        requestCount++;
        System.out.println("\nRequest " + requestCount + " started.");
        // return the listener instance that will handle this request.
        return new ApplicationRequestEventListener(requestCount);
    }
}
