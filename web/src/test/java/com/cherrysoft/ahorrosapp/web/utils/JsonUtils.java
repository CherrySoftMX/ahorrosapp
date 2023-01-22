package com.cherrysoft.ahorrosapp.web.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtils {

  public static String asJsonString(Object object) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
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
