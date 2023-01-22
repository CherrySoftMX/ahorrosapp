package com.cherrysoft.ahorrosapp.web.controllers;

import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.cherrysoft.ahorrosapp.common.repositories.UserRepository;
import com.cherrysoft.ahorrosapp.common.services.UserService;
import com.cherrysoft.ahorrosapp.web.utils.JsonUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIT {
  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private UserService userService;
  private User user;
  private String accessToken;

  @BeforeEach
  void setup() {
    user = User.builder()
        .username("hikingcarrot7")
        .password("password12345")
        .build();
    String unencryptedPassword = user.getPassword();
    userService.createUser(user);
    user.setPassword(unencryptedPassword);
  }

  @AfterEach
  void deleteUsers() {
    userRepository.deleteAll();
  }

  void doLogin() throws Exception {
    var body = Map.of("username", user.getUsername(), "password", user.getPassword());

    String response = mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJsonString(body))
        )
        .andDo(print())
        .andReturn()
        .getResponse()
        .getContentAsString();

    accessToken = JsonUtils.readValueFromJsonString(response, "accessToken");
  }

  @Test
  void whenUserIsValid_thenIsAddedToDatabase_and201StatusIsReturned() throws Exception {
    User providedUser = User.builder()
        .username("testUsername")
        .password("secure-password")
        .build();

    mockMvc
        .perform(
            post(UserController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJsonString(providedUser))
                .with(jwt())
        )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(header().string("Location", notNullValue()))
        .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));
  }

  @Test
  void whenUserIsInvalid_then400StatusIsReturned() throws Exception {
    User providedUser = User.builder()
        .username("hik")
        .password("1234")
        .build();

    mockMvc
        .perform(
            post(UserController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJsonString(providedUser))
                .with(jwt())
        )
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void whenUserIsDeleted_then200StatusIsReturned() throws Exception {
    doLogin();

    mockMvc
        .perform(
            delete("/users/hikingcarrot7")
                .header("Authorization", "Bearer " + accessToken)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.username").value("hikingcarrot7"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

}
