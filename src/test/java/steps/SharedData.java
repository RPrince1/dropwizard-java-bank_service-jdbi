package steps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class SharedData {

  public HashMap<String, Object> variables = new HashMap<>();
  public static final String HTTP_RESPONSE_CODE = "httpResponseCode";
  public static final String HTTP_RESPONSE_ENTITY = "httpResponseEntity";
  public static final String HTTP_RESPONSE_ENTITY_STRING = "httpResponseEntityString";


  public String readFile(String path) throws IOException {
    if (!path.equals("")) {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded);
    } else {
      return null;
    }
  }
}
