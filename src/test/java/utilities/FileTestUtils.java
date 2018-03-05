package utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class FileTestUtils {
  private static final ObjectMapper objMapper = new ObjectMapper();

  private FileTestUtils() {}

  public static <T> T fromFile(String path, Class<T> clazz) throws IOException {
    return objMapper.readValue(readFile(path), clazz);
  }

  public static String readFile(String path) throws IOException {
    return IOUtils.toString(FileTestUtils.class.getResourceAsStream(path));
  }

  public static String toJson(Object value) throws JsonProcessingException {
    return objMapper.writeValueAsString(value);
  }
}
