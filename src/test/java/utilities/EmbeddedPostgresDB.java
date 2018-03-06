package utilities;


import com.morrisons.base.dropwizard.config.ApplicationConfiguration;
import com.morrisons.base.dropwizard.config.JdbiProviderModule;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import io.dropwizard.db.DataSourceFactory;
import org.flywaydb.core.Flyway;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import ru.vyarus.dropwizard.guice.test.GuiceyAppRule;

import java.io.IOException;
import java.util.List;

public class EmbeddedPostgresDB {

    private EmbeddedPostgres db;

    public void stop() throws IOException {
        if(db != null)
        db.close();
    }

    public void startDatabase(DataSourceFactory database) throws IOException {
        int port = Integer.parseInt(database.getUrl().substring(database.getUrl().lastIndexOf(':') + 1, database.getUrl().lastIndexOf('/')));
        db = EmbeddedPostgres.builder().setPort(port).start();
        database.setPassword("postgres");
        database.setUser("postgres");
        database.setUrl(String.format("jdbc:postgresql://localhost:%s/postgres", db.getPort()));
    }

    public void setUpDatabase(String filesLocation, DataSourceFactory database) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(database.getUrl(), database.getUser(), database.getPassword());
        flyway.setLocations(filesLocation);
        flyway.migrate();
    }

    public void truncateTables(GuiceyAppRule<ApplicationConfiguration> rule, List<String> tableToClear) {
        DBI dbi = new JdbiProviderModule().prepareJdbi(rule.getEnvironment(), rule.getConfiguration());

        tableToClear.forEach(t -> truncateTable(dbi, t));
    }

    private void truncateTable(DBI dbi, String tableName) {
        Handle handle = dbi.open();
        handle.begin();
        handle.execute(String.format("TRUNCATE TABLE %s", tableName));
        handle.commit();
        handle.close();
    }
}
