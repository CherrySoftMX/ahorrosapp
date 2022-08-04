package com.cherrysoft.ahorrosapp.repositories;

import com.cherrysoft.ahorrosapp.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.core.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PiggyBankRepository extends JpaRepository<PiggyBank, Long> {

  Optional<PiggyBank> findByNameAndOwner(String name, User owner);

  boolean existsByNameAndOwner(String name, User owner);
}
