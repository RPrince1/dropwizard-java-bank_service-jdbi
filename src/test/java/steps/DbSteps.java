package steps;

import com.morrisons.base.dropwizard.dao.CustomerDao;
import com.morrisons.base.dropwizard.model.CustomerTransaction;
import com.morrisons.base.dropwizard.model.CustomerTransactions;
import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import utilities.EmbeddedPostgresDB;

import static com.morrisons.base.dropwizard.BaseDWApplication.getApplicationInjector;
import static com.morrisons.base.dropwizard.resources.ParameterConstants.ACCOUNT_ID;
import static com.morrisons.base.dropwizard.utils.JsonXmlUtils.fromJson;
import static com.morrisons.base.dropwizard.utils.JsonXmlUtils.readFile;
import static org.junit.Assert.assertEquals;
import static utilities.BaseConfigLoader.configuration;

public class DbSteps {


    private SharedData sharedData;
    private EmbeddedPostgresDB postgresDB = new EmbeddedPostgresDB();

    public DbSteps(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    @Given("^I have an embedded Postgres DB$")
    public void embeddedPostgresDB() throws Throwable {
        postgresDB.startDatabase(configuration.getDatabase());
        postgresDB.setUpDatabase(configuration.getFlyway().getMigrationFilesLocation(), configuration.getDatabase());
    }

    @And("^(\\d+) record should exist in the \"([^\"]*)\" table$")
    public void messagesShouldHaveBeenAddedToThe(int count, String table) throws Throwable {
        String accountId = (String) sharedData.variables.get(ACCOUNT_ID);

        CustomerDao customerDao = getApplicationInjector().getInstance(CustomerDao.class);
        int dbRecordCount = customerDao.getCustomerTransactions(accountId).size();
        assertEquals(count, dbRecordCount);
    }


    @After
    public void tearDown() throws Exception {
        if (postgresDB != null)postgresDB.stop();
    }

    @Given("^A transaction exists in the database from file \"([^\"]*)\"$")
    public void aTransactionExistsInTheDatabaseFromFile(String jsonFile) throws Throwable {

        CustomerDao customerDao = getApplicationInjector().getInstance(CustomerDao.class);
        CustomerTransaction txnToSave = fromJson(readFile(jsonFile), CustomerTransaction.class);

        customerDao.addTransaction(txnToSave);
    }
}
