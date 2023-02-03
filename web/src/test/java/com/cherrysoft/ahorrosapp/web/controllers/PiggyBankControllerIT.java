package com.cherrysoft.ahorrosapp.web.controllers;

import com.cherrysoft.ahorrosapp.web.AbstractControllerIT;
import com.cherrysoft.ahorrosapp.web.dtos.PiggyBankDTO;
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

import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.aWeekInTheFuture;
import static com.cherrysoft.ahorrosapp.common.utils.DateUtils.today;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/piggy_bank/data.sql")
@Sql(value = "/piggy_bank/clear-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PiggyBankControllerIT extends AbstractControllerIT {
  @MockBean(name = "clock") private Clock clock;

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
  @DisplayName("Get piggy bank endpoint")
  class GetPiggyBank {

    @Test
    void shouldReturnPaginatedUserPiggyBanks() throws Exception {
      doLogin();

      // First page
      mockMvc.perform(
              get("/")
                  .headers(authHeader())
          )
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
          .andExpect(jsonPath("$._links.prev").doesNotExist())
          .andExpect(jsonPath("$._links.next").exists())
          .andExpect(jsonPath("$._links.last").exists())
          .andExpect(jsonPath("$._links.self").exists())
          .andExpect(jsonPath("$._embedded.piggy_banks.size()", is(10)))
          .andExpect(jsonPath("$.page.number", is(0)))
          .andExpect(jsonPath("$.page.totalElements", is(15)))
          .andExpect(jsonPath("$.page.totalPages", is(2)));

      // Second page (last page)
      mockMvc.perform(
              get("/?page=1")
                  .headers(authHeader())
          )
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
          .andExpect(jsonPath("$._links.first").exists())
          .andExpect(jsonPath("$._links.prev").exists())
          .andExpect(jsonPath("$._links.next").doesNotExist())
          .andExpect(jsonPath("$._links.self").exists())
          .andExpect(jsonPath("$._embedded.piggy_banks.size()", is(5)))
          .andExpect(jsonPath("$.page.number", is(1)));
    }

    @Test
    void shouldGetPiggyBank() throws Exception {
      doLogin();

      mockMvc.perform(
              get("/myPiggy")
                  .headers(authHeader())
          )
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
          .andExpect(jsonPath("$.name").value("myPiggy"))
          .andExpect(jsonPath("$.startSavings").value("01-01-2023"))
          .andExpect(jsonPath("$.endSavings").value("31-01-2023"))
          .andExpect(jsonPath("$._links.self.href", is("http://localhost/myPiggy")))
          .andExpect(jsonPath("$._links.owner.href", is("http://localhost/users/hikingcarrot7")));
    }

    @Test
    void shouldReturn404_whenPiggyBankNotFound() throws Exception {
      doLogin();

      mockMvc.perform(
              get("/notFoundPiggy")
                  .headers(authHeader())
          )
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.detail").exists())
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

  }

  @Nested
  @DisplayName("Create piggy bank endpoint")
  class CreatePiggyBank {

    @Test
    void shouldCreatePiggyBank() throws Exception {
      doLogin();
      PiggyBankDTO providedPb = PiggyBankDTO.builder()
          .name("newPiggy")
          .startSavings(today())
          .endSavings(aWeekInTheFuture())
          .build();

      mockMvc.perform(
              post("/piggybank")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(providedPb))
                  .headers(authHeader())
          )
          .andExpect(status().isCreated())
          .andExpect(header().exists("Location"))
          .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
          .andExpect(jsonPath("$.id").exists())
          .andExpect(jsonPath("$.name").value("newPiggy"))
          .andExpect(jsonPath("$.startSavings").value("01-01-2023"))
          .andExpect(jsonPath("$.endSavings").value("08-01-2023"))
          .andExpect(jsonPath("$.initialAmount").value("0"))
          .andExpect(jsonPath("$.borrowedAmount").value("0"));
    }

    @Test
    void shouldSetStartDateToToday_whenNoSpecified() throws Exception {
      doLogin();
      PiggyBankDTO providedPb = PiggyBankDTO.builder()
          .name("newPiggy")
          .build();

      mockMvc.perform(
              post("/piggybank")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(providedPb))
                  .headers(authHeader())
          )
          .andExpect(status().isCreated())
          .andExpect(header().exists("Location"))
          .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
          .andExpect(jsonPath("$.id").exists())
          .andExpect(jsonPath("$.name").value("newPiggy"))
          .andExpect(jsonPath("$.startSavings").value("01-01-2023"))
          .andExpect(jsonPath("$.endSavings").doesNotExist());
    }

    @Test
    void shouldReturn400_whenStartDateIsAfterEndDate() throws Exception {
      doLogin();
      PiggyBankDTO providedPb = PiggyBankDTO.builder()
          .name("newPiggy")
          .startSavings(aWeekInTheFuture())
          .endSavings(today())
          .build();

      mockMvc.perform(
              post("/piggybank")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(providedPb))
                  .headers(authHeader())
          )
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.detail").exists())
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

  }

  @Nested
  @DisplayName("Update piggy bank endpoint")
  class UpdatePiggyBank {

    @Test
    void shouldPartiallyUpdatePiggyBank() throws Exception {
      doLogin();
      PiggyBankDTO updatedPb = PiggyBankDTO.builder()
          .name("updatePiggy")
          .initialAmount(BigDecimal.valueOf(100))
          .borrowedAmount(BigDecimal.valueOf(50))
          .endSavings(LocalDate.of(2023, Month.FEBRUARY, 9))
          .build();

      mockMvc.perform(
              patch("/myPiggy")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(updatedPb))
                  .headers(authHeader())
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value("updatePiggy"))
          .andExpect(jsonPath("$.initialAmount").value("100"))
          .andExpect(jsonPath("$.borrowedAmount").value("50"))
          .andExpect(jsonPath("$.startSavings").value("01-01-2023"))
          .andExpect(jsonPath("$.endSavings").value("09-02-2023"));
    }

    @Test
    void shouldReturn400_whenUpdatedEndDateIsBeforeStartDate() throws Exception {
      doLogin();
      PiggyBankDTO updatedPb = PiggyBankDTO.builder()
          .endSavings(LocalDate.of(2012, Month.DECEMBER, 19))
          .build();

      mockMvc.perform(
              patch("/myPiggy")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(updatedPb))
                  .headers(authHeader())
          )
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.detail").exists())
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

    @Test
    void shouldReturn404_whenPiggyBankNotFound() throws Exception {
      doLogin();
      PiggyBankDTO providedPb = PiggyBankDTO.builder()
          .name("updatedPiggy")
          .build();

      mockMvc.perform(
              patch("/notFoundPiggy")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(providedPb))
                  .headers(authHeader())
          )
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.detail").exists())
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

  }

  @Nested
  @DisplayName("Delete piggy bank endpoint")
  class DeletePiggyBank {

    @Test
    void shouldDeletePiggyBank() throws Exception {
      doLogin();

      mockMvc.perform(
              delete("/myPiggy")
                  .headers(authHeader())
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value("myPiggy"))
          .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldReturn404_whenPiggyBankNotFound() throws Exception {
      doLogin();

      mockMvc.perform(
              delete("/notFoundPiggy")
                  .headers(authHeader())
          )
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.detail").exists())
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

  }

}
