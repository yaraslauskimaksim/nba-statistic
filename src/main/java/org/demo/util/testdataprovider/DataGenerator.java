package org.demo.util.testdataprovider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DataGenerator {
  private static final Logger log = LogManager.getLogger(DataGenerator.class);

  public static <R> List<R> generateInput(String fileName, Function<String[], R> mapper) {
    List<R> data = new ArrayList<>();
    String line;
    String delimiter = ",";

    try (InputStream is = DataGenerator.class.getClassLoader().getResourceAsStream(fileName);
         BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      while ((line = br.readLine()) != null) {
        String[] fields = line.split(delimiter);
        var piece = mapper.apply(fields);
        data.add(piece);
      }
    } catch (IOException e) {
      log.debug(e);
    }

    return data;
  }
}
