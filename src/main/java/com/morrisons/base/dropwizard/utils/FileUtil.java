package com.morrisons.base.dropwizard.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

@UtilityClass
public class FileUtil {
  public static String fromFile(String path) {
    try {
      return IOUtils.toString(FileUtil.class.getResourceAsStream(path));
    } catch (IOException e) {
      throw new RuntimeException("Failed to read schema file: " + path, e);
    }
  }
}
