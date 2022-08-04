package com.cherrysoft.ahorrosapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

@Slf4j
public class App implements CommandLineRunner {

  @Override
  public void run(String... args) throws Exception {
    log.info("Common module");
  }
}
