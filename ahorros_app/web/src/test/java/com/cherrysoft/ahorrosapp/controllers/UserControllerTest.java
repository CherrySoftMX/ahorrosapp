package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTest {
  @MockBean private UserService userService;
  // @MockBean private UserMapper userMapper;
  @Autowired private MockMvc mockMvc;

  @Test
  void contextLoads() {
    assertThat(mockMvc).isNotNull();
  }
}