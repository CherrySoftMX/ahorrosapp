package com.cherrysoft.ahorrosapp.common.repositories;

import com.cherrysoft.ahorrosapp.common.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailySavingRepository extends JpaRepository<DailySaving, Long> {

  Optional<DailySaving> findByPiggyBankAndDate(PiggyBank piggyBank, LocalDate date);

  List<DailySaving> findByPiggyBankAndDateBetween(PiggyBank piggyBank, LocalDate startDate, LocalDate endDate);

}
