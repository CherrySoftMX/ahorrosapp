package com.cherrysoft.ahorrosapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.cherrysoft.ahorrosapp")
public class AhorrosApp {

  public static void main(String[] args) {
    SpringApplication.run(AhorrosApp.class, args);
  }
}
