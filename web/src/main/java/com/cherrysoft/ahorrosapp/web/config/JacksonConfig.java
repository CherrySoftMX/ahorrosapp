package com.cherrysoft.ahorrosapp.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.validation.ConstraintViolationProblemModule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.DAY_MONTH_YEAR_PATTERN;


@Configuration
@RequiredArgsConstructor
public class JacksonConfig {
  @Value("${response.show.stacktrace:false}")
  private boolean showStackTraces;

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return mapperBuilder -> {
      var localDateFormatter = DateTimeFormatter.ofPattern(DAY_MONTH_YEAR_PATTERN);
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

  @Bean
  public ProblemModule problemModule() {
    return new ProblemModule().withStackTraces(showStackTraces);
  }

  @Bean
  public ConstraintViolationProblemModule constraintViolationProblemModule() {
    return new ConstraintViolationProblemModule();
  }

}
