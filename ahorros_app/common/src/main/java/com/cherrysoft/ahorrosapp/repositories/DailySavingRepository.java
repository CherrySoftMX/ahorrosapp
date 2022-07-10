package com.cherrysoft.ahorrosapp.repositories;

import com.cherrysoft.ahorrosapp.core.models.DailySaving;
import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailySavingRepository extends JpaRepository<DailySaving, Long> {

  Optional<DailySaving> findByPiggyBankAndDate(PiggyBank piggyBank, LocalDate date);

}
