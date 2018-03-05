package utilities;

import com.morrisons.base.dropwizard.config.ApplicationConfiguration;
import com.morrisons.base.dropwizard.config.GlobalParametersConfiguration;
import de.thomaskrille.dropwizard_template_config.redist.freemarker.template.Configuration;
import de.thomaskrille.dropwizard_template_config.redist.freemarker.template.Template;
import de.thomaskrille.dropwizard_template_config.redist.freemarker.template.TemplateExceptionHandler;
import de.thomaskrille.dropwizard_template_config.redist.freemarker.template.Version;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;

import javax.validation.Validation;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class BaseConfigLoader {
    private static final String ENV = "ENV";
    public static ApplicationConfiguration configuration;

    static {
        configuration = readConfiguration();
    }

    private static ApplicationConfiguration readConfiguration() {

        try {
            parseTemplate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        ConfigurationFactory<ApplicationConfiguration> configurationFactory = new YamlConfigurationFactory<>(
                ApplicationConfiguration.class,
                Validation.buildDefaultValidatorFactory().getValidator(),
                Jackson.newObjectMapper(),
                "");
        try {
            return configurationFactory.build(new File(getConfigFile()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getConfigFile() {
//        if (getenv(ENV) == null || getenv(ENV).isEmpty()) {
//            return "config/application_CI.yml";
//        }
//        return "config/application_" + getenv(ENV) + ".yml";

        return "mergedConfig.yml";
    }

    private static void parseTemplate() throws Exception {

        GlobalParametersConfiguration globalConfig = GlobalParametersConfiguration.builder().configFileName("config/application_CI.yml").build();

        Map<String, Object> dataModel = new HashMap<>();

        dataModel.put(globalConfig.getNamespace(),globalConfig.getVariables());

        // 1. Configure FreeMarker
        //
        // You should do this ONLY ONCE, when your application starts,
        // then reuse the same Configuration object elsewhere.

        Configuration cfg = new Configuration();



        // Where do we load the templates from:
        //cfg.setClassForTemplateLoading(VoucherApplication.class, "config");

        cfg.setDirectoryForTemplateLoading(new File("config"));
        // Some other recommended settings:
        cfg.setNumberFormat("computer");
        cfg.setIncompatibleImprovements(new Version(2, 3, 20));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.UK);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // 2. Proccess template(s)
        //
        // You will do this for several times in typical applications.

        // 2.1. Prepare the template input:

        // 2.2. Get the template

        Template template = cfg.getTemplate("application_CI.yml");

        // 2.3. Generate the output

        // Write output to the console
        Writer consoleWriter = new OutputStreamWriter(System.out);
        template.process(dataModel, consoleWriter);

        // For the sake of example, also write output into a file:
        Writer fileWriter = new FileWriter(new File("mergedConfig.yml"));
        try {
            template.process(dataModel, fileWriter);
        } finally {
            fileWriter.close();
        }

    }


}
