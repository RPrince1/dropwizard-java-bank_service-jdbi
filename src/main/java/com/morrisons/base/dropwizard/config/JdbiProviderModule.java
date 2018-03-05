package com.morrisons.base.dropwizard.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.morrisons.base.dropwizard.dao.CustomerDao;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class JdbiProviderModule extends AbstractModule {

    private DBI dbi;

    @Provides
    public DBI prepareJdbi(Environment environment, ApplicationConfiguration configuration) {
        if (dbi == null) {
            final DBIFactory factory = new DBIFactory();
            dbi = factory.build(environment, configuration.getDatabase(), "postgres");
        }
        return dbi;
    }

    @Override
    protected void configure() {
    }

    @Provides
    public CustomerDao customerDao(DBI dbi) {
        return dbi.onDemand(CustomerDao.class);
    }
}
