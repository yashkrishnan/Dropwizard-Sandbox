package com.test.sandbox.healthchecks;

import com.codahale.metrics.health.HealthCheck;

/**
 * General health check class for bypassing "no health check warning"
 */
public class GenericHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}