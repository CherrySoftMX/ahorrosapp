package com.cherrysoft.ahorrosapp.common.config;

import com.github.javafaker.Faker;

import java.util.Locale;

public class FakerConfig {
  public static final Faker FAKER_INSTANCE = new Faker(new Locale("es-MX"));
}
