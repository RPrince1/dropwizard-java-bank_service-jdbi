package steps;

import org.junit.After;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class SharedData {

  public HashMap<String, Object> variables = new HashMap<>();

  public ClientAndServer salesforceMockServer;
  public ClientAndServer unicaMockServer;

  public int countInstances(Object object, String fieldName) throws Exception {
    Field[] fields = object.getClass().getDeclaredFields();
    int count = 0;
    for (Field field : fields) {
      field.setAccessible(true);
      if (null != field.get(object)) {
        if (fieldName.equals(field.getName())) {
          count++;
        }
      }
    }
    return count;
  }

  public String readFile(String path) throws IOException {
    if (!path.equals("")) {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded);
    }
    else {
      return null;
    }
  }

  public void startGETMockServer(String uri, int port, String responseBody) throws Exception {
    new MockServerClient("localhost", port)
      .when(
        request()
          .withMethod("GET")
          .withPath(uri),
        exactly(1)
      )
      .respond(
        response()
          .withStatusCode(200)
          .withHeaders(
            new Header("Content-Type", "application/json; charset=UTF-8")
          )
          .withBody(Files.readAllBytes(Paths.get(responseBody)))
      );
  }

  public void startPATCHMockServer(String uri, int port) throws Exception {
    new MockServerClient("localhost", port)
      .when(
        request()
          .withMethod("PATCH")
          .withPath(uri),
        exactly(2)
      )
      .respond(
        response()
          .withStatusCode(201)
          .withHeaders(
            new Header("Content-Type", "application/json; charset=utf-8")
          )
      );
  }

  public void startPOSTMockServer(String uri, int port) throws Exception {
    new MockServerClient("localhost", port)
      .when(
        request()
          .withMethod("POST")
          .withPath(uri),
        exactly(1)
      )
      .respond(
        response()
          .withStatusCode(204)
          .withHeaders(
            new Header("Content-Type", "application/json; charset=utf-8")
          )
      );
  }

  @After
  public void tearDown() {
    if (salesforceMockServer != null) {
      salesforceMockServer.stop();
    }
    if (unicaMockServer != null) {
      unicaMockServer.stop();
    }
  }

}
