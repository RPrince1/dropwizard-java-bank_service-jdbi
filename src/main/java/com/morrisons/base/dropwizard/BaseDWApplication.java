package com.morrisons.base.dropwizard;

import com.google.inject.Injector;
import com.morrisons.base.dropwizard.config.ApplicationConfiguration;
import com.morrisons.base.dropwizard.config.ConfigurationProviders;
import com.morrisons.base.dropwizard.config.JdbiProviderModule;
import com.morrisons.base.dropwizard.filters.ApplicationExceptionMapper;
import com.morrisons.base.dropwizard.filters.GeneralExceptionMapper;
import com.morrisons.base.dropwizard.healthcheck.AppHealthCheck;
import com.morrisons.base.dropwizard.resources.Resource;
import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.config.ScannerFactory;
import io.swagger.jaxrs.config.DefaultJaxrsScanner;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.flywaydb.core.Flyway;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.skife.jdbi.v2.DBI;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import ru.vyarus.dropwizard.guice.injector.lookup.InjectorProvider;
import ru.vyarus.dropwizard.guice.module.installer.feature.jersey.ResourceInstaller;

public class BaseDWApplication extends Application<ApplicationConfiguration> {

    private static Environment applicationEnvironment;
    private InjectorProvider provider;
    private static DBI applicationJDBI;
    private static Injector applicationInjector;
    public static ApplicationConfiguration configuration;

    public static Injector getApplicationInjector() {
        return applicationInjector;
    }

    private static void setApplicationInjector(Injector applicationInjector) {
        BaseDWApplication.applicationInjector = applicationInjector;
    }

    public static void main(String[] args) throws Exception {
        new BaseDWApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {
        provider = new InjectorProvider(this);

        GuiceBundle<ApplicationConfiguration> guiceBundle = GuiceBundle.<ApplicationConfiguration>builder()
                .installers(ResourceInstaller.class)
                .extensions(Resource.class)
                .modules(new ConfigurationProviders(), new JdbiProviderModule())
                .build();
        bootstrap.addBundle(guiceBundle);
        bootstrap.addBundle(new TemplateConfigBundle());

        if (!isProduction()) {
            bootstrap.addBundle(new AssetsBundle("/swagger-ui", "/api-docs", "index.html"));
        }
    }


    @Override
    public void run(ApplicationConfiguration configuration, Environment environment) throws Exception {
        setApplicationConfiguration(configuration);
        runDropWizard(environment);
    }

    private void runDropWizard(Environment environment) {
        setApplicationEnvironment(environment);

        final DBIFactory factory = new DBIFactory();
        setJDBI(factory.build(environment, configuration.getDatabase(), "postgresql"));
        DataSourceFactory dataSourceFactory = configuration.getDatabase();

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSourceFactory.getUrl(), dataSourceFactory.getUser(), dataSourceFactory.getPassword());
        flyway.setLocations(configuration.getFlyway().getMigrationFilesLocation());
        flyway.migrate();

        environment.healthChecks().register("appHealthcheck", new AppHealthCheck());
        environment.jersey().register(Resource.class);
        environment.jersey().register(MultiPartFeature.class);
        environment.jersey().register(new GeneralExceptionMapper());
        environment.jersey().register(new ApplicationExceptionMapper());
        environment.jersey().register(ApiListingResource.class);
        environment.jersey().register(SwaggerSerializers.class);
        ScannerFactory.setScanner(new DefaultJaxrsScanner());

        setApplicationInjector(provider.get());
    }

    private boolean isProduction() {
        String env = System.getenv("ENVIRONMENT");
        return env != null && "production".equals(env);
    }

    private static void setJDBI(DBI jdbi) {
        applicationJDBI = jdbi;
    }

    private static void setApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
        configuration = applicationConfiguration;
    }

    private static void setApplicationEnvironment(Environment environment) {
        applicationEnvironment = environment;
    }

    public static void stop() throws Exception {
        applicationEnvironment.getApplicationContext().getServer().stop();
    }

}
