package com.cherrysoft.ahorrosapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class AhorrosApplication {

  public static void main(String[] args) {
    SpringApplication.run(AhorrosApplication.class, args);
  }

}
