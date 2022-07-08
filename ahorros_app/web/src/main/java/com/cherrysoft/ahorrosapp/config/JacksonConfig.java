package com.cherrysoft.ahorrosapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import static com.cherrysoft.ahorrosapp.models.DailySaving.DAY_MONTH_YEAR;

@Configuration
public class JacksonConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return mapperBuilder -> {
      var localDateFormatter = DateTimeFormatter.ofPattern(DAY_MONTH_YEAR);
      mapperBuilder.serializerByType(LocalDate.class, new LocalDateSerializer(localDateFormatter));
      mapperBuilder.deserializerByType(LocalDate.class, new LocalDateDeserializer(localDateFormatter));
    };
  }

  @Bean
  public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
    return builder
        .createXmlMapper(false)
        .timeZone(TimeZone.getDefault())
        .build();
  }

}
