package com.morrisons.base.dropwizard.healthcheck;

import com.codahale.metrics.health.HealthCheck;

/**
 * Created by sysdevjm on 01/08/2016.
 */
public class AppHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
