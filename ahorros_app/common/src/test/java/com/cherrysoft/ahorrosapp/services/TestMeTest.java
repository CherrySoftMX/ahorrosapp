package com.cherrysoft.ahorrosapp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestMeTest {
  private TestMe testMe;

  @BeforeEach
  private void setup() {
    testMe = new TestMe();
  }

  @Test
  public void testSample() {
    assertEquals("test", testMe.sampleTest());
  }
}