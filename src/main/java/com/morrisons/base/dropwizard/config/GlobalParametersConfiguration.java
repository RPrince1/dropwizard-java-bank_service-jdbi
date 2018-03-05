package com.morrisons.base.dropwizard.config;

import com.morrisons.base.dropwizard.exceptions.ApplicationException;
import com.morrisons.base.dropwizard.exceptions.ErrorSequences;
import de.thomaskrille.dropwizard_template_config.TemplateConfigVariablesProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class GlobalParametersConfiguration implements TemplateConfigVariablesProvider {

    public static final String GLOBALS = "globals";
    private String configFileName;

    @Builder
    public GlobalParametersConfiguration(String configFileName) {
        this.configFileName = configFileName;
    }

    @Override
    public String getNamespace() {
        return GLOBALS;
    }

    @Override
    public Map<String, String> getVariables() {

        Yaml yaml = new Yaml();

        try {
            Map<String, Map<String, String>> values = (Map<String, Map<String, String>>) yaml
                    .load(new FileInputStream(new File(this.configFileName)));

            Map<String, String> map = values.get(GLOBALS);

            return map;

        } catch (FileNotFoundException e) {

            log.error(e.getMessage(), e);
            throw new ApplicationException(ErrorSequences.SPECIFIED_CONFIGURATION_FILE_COULD_NOT_BE_LOADED);
        }
    }
}
