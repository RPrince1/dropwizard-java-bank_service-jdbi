package com.morrisons.base.dropwizard.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class FlywayConfiguration {
    @NotNull
    @JsonProperty("migrationFilesLocation")
    private String migrationFilesLocation;
}
