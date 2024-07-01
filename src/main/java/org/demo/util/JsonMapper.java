package org.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.demo.exception.ReadJsonException;
import org.demo.exception.WriteJsonException;

import java.io.IOException;
import java.io.InputStream;

public class JsonMapper {

  private static final ObjectMapper OBJECT_MAPPER = configureObjectMapper();

  private static ObjectMapper configureObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }

  public static <T> byte[] toBytes(T source) {
    try {
      return OBJECT_MAPPER.writeValueAsBytes(source);
    } catch (JsonProcessingException ex) {
      throw new WriteJsonException(TextUtil.formatText("Failed to parse %s as byte[]", source), ex);
    }
  }

  public static <T> T fromInputStream(InputStream input, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.readValue(input, clazz);
    } catch (IOException ex) {
      throw new ReadJsonException(TextUtil.formatText("Failed parse input as object"), ex);
    }
  }

  public static <T> String toJson(T source) {
    try {
      return OBJECT_MAPPER.writeValueAsString(source);
    } catch (JsonProcessingException ex) {
      throw new WriteJsonException(TextUtil.formatText("Failed to parse %s as json", source), ex);
    }
  }

  public static <T> T fromJson(String json, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.readValue(json, clazz);
    } catch (JsonProcessingException ex) {
      throw new ReadJsonException(TextUtil.formatText("Failed parse %s json as object", json), ex);
    }
  }
}
