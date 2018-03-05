package com.morrisons.base.dropwizard.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.HttpClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
public class ApplicationConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private HttpClientConfiguration httpClient = new HttpClientConfiguration();

    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();

    @NotNull
    @JsonProperty
    private FlywayConfiguration flyway = new FlywayConfiguration();

}
