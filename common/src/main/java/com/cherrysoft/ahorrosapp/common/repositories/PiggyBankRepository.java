package com.cherrysoft.ahorrosapp.common.repositories;

import com.cherrysoft.ahorrosapp.common.core.models.PiggyBank;
import com.cherrysoft.ahorrosapp.common.core.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PiggyBankRepository extends JpaRepository<PiggyBank, Long> {

  @Query("select p from PiggyBank p where p.owner.username = :username")
  Page<PiggyBank> findAllByOwnerUsername(String username, Pageable pageable);

  Optional<PiggyBank> findByNameAndOwner(String name, User owner);

  boolean existsByNameAndOwner(String name, User owner);

}
