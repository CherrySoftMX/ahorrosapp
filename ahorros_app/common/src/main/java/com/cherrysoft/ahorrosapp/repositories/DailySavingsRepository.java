package com.cherrysoft.ahorrosapp.repositories;

import com.cherrysoft.ahorrosapp.models.DailySaving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailySavingsRepository extends JpaRepository<DailySaving, Long> {

}
