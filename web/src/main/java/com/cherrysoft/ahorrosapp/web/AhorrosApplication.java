package com.cherrysoft.ahorrosapp.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication(scanBasePackages = {"com.cherrysoft.ahorrosapp"})
@EntityScan(basePackages = "com.cherrysoft.ahorrosapp")
@EnableJpaRepositories(basePackages = "com.cherrysoft.ahorrosapp")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class AhorrosApplication {

  public static void main(String[] args) {
    SpringApplication.run(AhorrosApplication.class, args);
  }

}
