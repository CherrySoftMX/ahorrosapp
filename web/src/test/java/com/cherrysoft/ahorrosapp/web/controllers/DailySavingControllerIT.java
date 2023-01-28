package com.cherrysoft.ahorrosapp.web.controllers;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.web.AbstractControllerIT;
import com.cherrysoft.ahorrosapp.web.utils.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/daily_savings/data.sql")
@Sql(value = "/daily_savings/clear-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DailySavingControllerIT extends AbstractControllerIT {
  @MockBean private Clock clock;

  /**
   * From the test perspective, "today" is always January 1st, 2023
   */
  @BeforeEach
  void setup() {
    LocalDate january1st2023 = LocalDate.of(2023, Month.JANUARY, 1);
    ZoneId zoneId = ZoneId.systemDefault();
    given(clock.instant()).willReturn(january1st2023.atStartOfDay(zoneId).toInstant());
    given(clock.getZone()).willReturn(zoneId);
  }

  @Nested
  @DisplayName("Get daily saving endpoint")
  class GetDailySaving {

    @Test
    void shouldReturnTodayDailySaving() throws Exception {
      doLogin();

      mockMvc.perform(
              get("/myPiggy/daily").headers(authHeader())
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").exists())
          .andExpect(jsonPath("$.amount").exists())
          .andExpect(jsonPath("$.date").value("01-01-2023"))
          .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));
    }

    @Test
    void shouldReturnSpecifiedDailySaving() throws Exception {
      String january27th2023 = "27-01-2023";
      doLogin();

      mockMvc.perform(
              get("/myPiggy/daily?date=" + january27th2023).headers(authHeader())
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").exists())
          .andExpect(jsonPath("$.amount").exists())
          .andExpect(jsonPath("$.date").value(january27th2023))
          .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));
    }

    @Test
    void shouldReturn404_whenTodayDailySavingNotFound() throws Exception {
      LocalDate december19th2012 = LocalDate.of(2012, Month.DECEMBER, 19);
      ZoneId zoneId = ZoneId.systemDefault();
      given(clock.instant()).willReturn(december19th2012.atStartOfDay(zoneId).toInstant());
      given(clock.getZone()).willReturn(zoneId);
      doLogin();

      mockMvc.perform(
              get("/myPiggy/daily").headers(authHeader())
          )
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.detail").value("No saving for date: 19-12-2012"))
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

    @Test
    void shouldReturn404_whenDailySavingNotFound() throws Exception {
      String february1st2023 = "01-02-2023";
      doLogin();

      mockMvc.perform(
              get("/myPiggy/daily?date=" + february1st2023).headers(authHeader())
          )
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.detail").value("No saving for date: 01-02-2023"))
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

    @Test
    void shouldReturn400_whenInvalidDailySaving() throws Exception {
      mockMvc.perform(
              get("/myPiggy/daily?date=invalid-date").with(jwt())
          )
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.detail").exists())
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

  }

  @Nested
  @DisplayName("Create daily saving endpoint")
  class CreateDailySaving {

    @Test
    void shouldReturn201_whenCreationSuccessful() throws Exception {
      doLogin();
      String january31th2023 = "31-01-2023";
      DailySaving dailySaving = DailySaving.builder()
          .amount(BigDecimal.valueOf(100))
          .description("Some description")
          .build();

      mockMvc.perform(
              post("/myPiggy/daily?date=" + january31th2023)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(dailySaving))
                  .headers(authHeader())
          )
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").exists())
          .andExpect(jsonPath("$.amount").value("100"))
          .andExpect(jsonPath("$.date").value(january31th2023))
          .andExpect(jsonPath("$.description").value("Some description"))
          .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
          .andExpect(jsonPath("$._links.self.href", is("http://localhost/myPiggy/daily?date=31-01-2023")))
          .andExpect(jsonPath("$._links.piggy_bank.href", is("http://localhost/myPiggy")));
    }

    @Test
    void shouldOverrideDailySaving_whenAlreadyExists() throws Exception {
      doLogin();
      String january1st2023 = "01-01-2023";
      DailySaving dailySaving = DailySaving.builder()
          .amount(BigDecimal.valueOf(500))
          .description("New description")
          .build();

      mockMvc.perform(
              post("/myPiggy/daily?date=" + january1st2023)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(dailySaving))
                  .headers(authHeader())
          )
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.amount").value("500"))
          .andExpect(jsonPath("$.date").value(january1st2023))
          .andExpect(jsonPath("$.description").value("New description"))
          .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));
    }

    @Test
    void shouldReturn400_whenDailySavingDateIsOutOfPiggyBankDateRange() throws Exception {
      doLogin();
      String february9th2023 = "09-02-2023";
      DailySaving dailySaving = DailySaving.builder()
          .amount(BigDecimal.valueOf(100))
          .description("Simple description")
          .build();

      mockMvc.perform(
              post("/myPiggy/daily?date=" + february9th2023)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(dailySaving))
                  .headers(authHeader())
          )
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.detail").value("The saving with date: 09-02-2023 is out of date range: (01-01-2023 - 31-01-2023)"))
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

    @Test
    void shouldReturn400_whenAmountNotProvided() throws Exception {
      String january1st2023 = "01-01-2023";
      DailySaving emptySaving = DailySaving.builder().build();

      mockMvc.perform(
              post("/myPiggy/daily?date=" + january1st2023)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(emptySaving))
                  .with(jwt())
          )
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.title").value("Constraint Violation"))
          .andExpect(jsonPath("$.violations").exists())
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

  }

  @Nested
  @DisplayName("Update daily saving endpoint")
  class UpdateDailySaving {

    @Test
    void shouldAllowPartialUpdate() throws Exception {
      doLogin();
      String january1st2023 = "01-01-2023";
      DailySaving updatedSaving = DailySaving.builder()
          .amount(BigDecimal.valueOf(500.50))
          .build();

      mockMvc.perform(
              patch("/myPiggy/daily?date=" + january1st2023)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(updatedSaving))
                  .headers(authHeader())
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.date").value("01-01-2023"))
          .andExpect(jsonPath("$.amount").value("500.5"))
          .andExpect(jsonPath("$.description").value("Sample description"))
          .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
          .andExpect(jsonPath("$._links.self.href", is("http://localhost/myPiggy/daily?date=01-01-2023")))
          .andExpect(jsonPath("$._links.piggy_bank.href", is("http://localhost/myPiggy")));
    }

    @Test
    void shouldReturn404_whenDailySavingNotFound() throws Exception {
      String january31th2023 = "31-01-2023";
      doLogin();
      DailySaving updatedSaving = DailySaving.builder()
          .amount(BigDecimal.valueOf(500.50))
          .build();

      mockMvc.perform(
              patch("/myPiggy/daily?date=" + january31th2023)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(updatedSaving))
                  .headers(authHeader())
          )
          .andExpect(status().isNotFound())
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

  }

  @Nested
  @DisplayName("Delete daily saving endpoint")
  class DeleteDailySaving {

    @Test
    void shouldDeleteDailySaving() throws Exception {
      String january1st2023 = "01-01-2023";
      doLogin();

      mockMvc.perform(
              delete("/myPiggy/daily?date=" + january1st2023)
                  .headers(authHeader())
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").exists())
          .andExpect(jsonPath("$.date").value("01-01-2023"))
          .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldReturn404_whenDailySavingNotFound() throws Exception {
      String january31th2023 = "31-01-2023";
      doLogin();

      mockMvc.perform(
              delete("/myPiggy/daily?date=" + january31th2023)
                  .headers(authHeader())
          )
          .andExpect(status().isNotFound())
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

  }

}