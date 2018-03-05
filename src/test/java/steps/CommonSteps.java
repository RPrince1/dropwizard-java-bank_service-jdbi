package steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morrisons.base.dropwizard.BaseDWApplication;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;

import static org.apache.http.protocol.HTTP.USER_AGENT;

/**
 * Created by Rich on 08/09/2016.
 */
public class CommonSteps {

  private SharedData sharedData;

  private String baseUri = "http://localhost:%s/club/v1%s";

  private String basePort = "8090";

  public CommonSteps(SharedData sharedData) {
    this.sharedData = sharedData;
  }

  @After
  public void tearDown() throws Exception {
    BaseDWApplication.stop();
    if (sharedData.salesforceMockServer != null) {
      sharedData.salesforceMockServer.stop();
    }
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

    sharedData.variables.put("httpResponseCode", response.getStatusLine().getStatusCode());

    sharedData.variables.put("httpResponseEntity", response);
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

  @And("^I perform a PUT on \"([^\"]*)\" with data \"([^\"]*)\"$")
  public void iPerformAPUTOnWithData(String uri, String jsonFile) throws Throwable {
    HttpPut request = new HttpPut(String.format(baseUri, basePort, uri));

    HttpClient httpClient = HttpClientBuilder.create().build();
    request.addHeader("User-Agent", USER_AGENT);
    request.addHeader("Content-type", "application/json");
    request.setEntity(new StringEntity(sharedData.readFile(jsonFile)));
    HttpResponse response = httpClient.execute(request);

    sharedData.variables.put("httpResponseCode", response.getStatusLine().getStatusCode());

    sharedData.variables.put("httpResponseEntity", response);
  }

  @And("^I perform a DELETE on \"([^\"]*)\"$")
  public void iPerformADELETEOn(String uri) throws Throwable {
    HttpDelete request = new HttpDelete(String.format(baseUri, basePort, uri));

    HttpClient httpClient = HttpClientBuilder.create().build();
    request.addHeader("User-Agent", USER_AGENT);
    request.addHeader("Content-type", "application/json");
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



  @And("^The response code should be \"([^\"]*)\"$")
  public void theResponseCodeShouldBe(String responseCode) throws Throwable {
    String response = sharedData.variables.get("httpResponseCode").toString();
    Assert.assertEquals(responseCode, response);
  }
}
