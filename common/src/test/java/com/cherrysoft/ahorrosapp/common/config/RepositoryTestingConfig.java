package com.cherrysoft.ahorrosapp.common.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.cherrysoft.ahorrosapp.common")
@EnableJpaRepositories("com.cherrysoft.ahorrosapp.common")
public class RepositoryTestingConfig {
}
