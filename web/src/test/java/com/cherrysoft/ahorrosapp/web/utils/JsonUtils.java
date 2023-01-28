package com.cherrysoft.ahorrosapp.web.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtils {

  public static String asJsonString(Object object) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      mapper.registerModule(new JavaTimeModule());
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static String readValueFromJsonString(String json, String key) {
    try {
      ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
      return node.get(key).asText();
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
