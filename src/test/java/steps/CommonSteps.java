package steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morrisons.base.dropwizard.BaseDWApplication;

import com.morrisons.base.dropwizard.model.Banks;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import static com.morrisons.base.dropwizard.resources.ParameterConstants.ACCOUNT_ID;
import static com.morrisons.base.dropwizard.utils.JsonXmlUtils.fromJson;
import static org.apache.http.protocol.HTTP.USER_AGENT;
import static org.junit.Assert.assertEquals;
import static steps.SharedData.*;

/**
 * Created by Rich on 08/09/2016.
 */
public class CommonSteps {

    private SharedData sharedData;

    private String baseUri = "http://localhost:%s/example-dropwizard/v1%s";

    private String basePort = "8090";

    public CommonSteps(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    @After
    public void tearDown() throws Exception {
        BaseDWApplication.stop();
    }

    @Given("^The service is running$")
    public void theServiceIsRunning() throws Throwable {
        String[] args = new String[2];
        args[0] = "server";
        args[1] = "config/application_CI.yml";
        BaseDWApplication.main(args);
    }

    @And("^I perform a GET on \"([^\"]*)\"$")
    public void iPerformAGETOn(String uri) throws Throwable {
        HttpGet request = new HttpGet(String.format(baseUri, basePort, uri));

        HttpClient httpClient = HttpClientBuilder.create().build();
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(request);

        sharedData.variables.put(HTTP_RESPONSE_CODE, response.getStatusLine().getStatusCode());
        sharedData.variables.put(HTTP_RESPONSE_ENTITY, response);
        sharedData.variables.put(HTTP_RESPONSE_ENTITY_STRING, EntityUtils.toString(response.getEntity()));
    }

    @And("^I perform a POST on \"([^\"]*)\" with data \"([^\"]*)\"$")
    public void iPerformAPOSTOnWithData(String uri, String jsonFile) throws Throwable {
        HttpPost request = new HttpPost(String.format(baseUri, basePort, uri));

        HttpClient httpClient = HttpClientBuilder.create().build();
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Content-type", "application/json");
        if (!jsonFile.equals("")) {
            request.setEntity(new StringEntity(sharedData.readFile(jsonFile)));
        }
        HttpResponse response = httpClient.execute(request);

        sharedData.variables.put("httpResponseCode", response.getStatusLine().getStatusCode());

        sharedData.variables.put("httpResponseEntity", response);
    }


    @Then("^The response should map to \"([^\"]*)\"$")
    public void theResponseShouldMapTo(String classMapping) throws Throwable {
        Class marshallClass = Class.forName("com.morrisons.clubs.model.entity." + classMapping);

        HttpResponse response = (HttpResponse) sharedData.variables.get("httpResponseEntity");

        ObjectMapper objectMapper = new ObjectMapper();
        Object responseObject = objectMapper.readValue(response.getEntity().getContent(), marshallClass);

        sharedData.variables.put("responseObject", responseObject);
    }

    @And("^I am customer with account ID \"([^\"]*)\"$")
    public void iAmCustomerWithAccountID(String accountId) throws Throwable {
        sharedData.variables.put(ACCOUNT_ID, accountId);
    }

    @And("^The response code should be \"([^\"]*)\"$")
    public void theResponseCodeShouldBe(String responseCode) throws Throwable {
        String response = sharedData.variables.get("httpResponseCode").toString();
        assertEquals(responseCode, response);
    }

    @Then("^I expect (\\d+) transaction in the response$")
    public void iExpectTransactionInTheResponse(int count) throws Throwable {
        String responseString = (String) sharedData.variables.get(HTTP_RESPONSE_ENTITY_STRING);

        Banks customerTransactions = fromJson(responseString, Banks.class);
        int numberOfTransactionsInResponse = customerTransactions.getBanks().size();

        assertEquals(count, numberOfTransactionsInResponse);
    }
}
