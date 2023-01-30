package com.cherrysoft.ahorrosapp.common.repositories;

import com.cherrysoft.ahorrosapp.common.config.RepositoryTestingConfig;
import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.SavingsDateRange;
import com.cherrysoft.ahorrosapp.common.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = {DailySavingRepository.class, PiggyBankRepository.class})
@Import(RepositoryTestingConfig.class)
@ActiveProfiles("test")
class DailySavingRepositoryIT {
  @Autowired private DailySavingRepository dailySavingRepository;
  @Autowired private PiggyBankRepository pbRepository;
  private PiggyBank pb;

  @BeforeEach
  void setup() {
    String january2023 = "01-2023";
    PiggyBank newPb = PiggyBank.builder()
        .name("test-piggy")
        .savingsDateRange(SavingsDateRange.forMonth(january2023))
        .build();
    List<DailySaving> savings = TestUtils.Savings.generateSavingsForMonth(january2023);
    newPb.addDailySavings(savings);
    pb = pbRepository.saveAndFlush(newPb);
  }

  @AfterEach
  void deleteAll() {
    dailySavingRepository.deleteAll();
    pbRepository.deleteAll();
  }

  @Nested
  @DisplayName("Find daily saving by date")
  class FindDailySavingByDate {

    @Test
    void shouldReturnDailySaving() {
      Optional<DailySaving> firstMonthSaving = dailySavingRepository.findByPiggyBankAndDate(pb, LocalDate.of(2023, Month.JANUARY, 1));

      assertTrue(firstMonthSaving.isPresent());
    }

    @Test
    void shouldReturnEmptyDailySaving_whenInvalidDate() {
      LocalDate invalidDate = LocalDate.of(2012, Month.DECEMBER, 19);
      Optional<DailySaving> maybeSaving = dailySavingRepository.findByPiggyBankAndDate(pb, invalidDate);

      assertTrue(maybeSaving.isEmpty());
    }

  }

  @Nested
  @DisplayName("Find daily savings between dates")
  class FindDailySavingsBetweenDates {

    @Test
    void shouldReturnDailySavingsBetweenDates() {
      List<DailySaving> dailySavings = dailySavingRepository.findByPiggyBankAndDateBetween(pb, LocalDate.of(2023, Month.JANUARY, 1), LocalDate.of(2023, Month.JANUARY, 7));

      assertThat(dailySavings, hasSize(7));
    }

  }


}