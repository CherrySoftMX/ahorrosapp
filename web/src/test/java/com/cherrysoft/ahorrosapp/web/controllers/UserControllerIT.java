package com.cherrysoft.ahorrosapp.web.controllers;

import com.cherrysoft.ahorrosapp.common.repositories.UserRepository;
import com.cherrysoft.ahorrosapp.common.services.UserService;
import com.cherrysoft.ahorrosapp.web.AbstractControllerIT;
import com.cherrysoft.ahorrosapp.web.utils.JsonUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerIT extends AbstractControllerIT {
  @Autowired private UserService userService;
  @Autowired private UserRepository userRepository;

  @BeforeEach
  void registerUser() {
    userService.createUser(getUserForLogin());
  }

  @AfterEach
  void deleteUsers() {
    userRepository.deleteAll();
  }

  @Nested
  class GetUser {

    @Test
    void shouldGetUserByUsername() throws Exception {
      doLogin();

      mockMvc.perform(
              get("/users/hikingcarrot7")
                  .headers(authHeader())
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").exists())
          .andExpect(jsonPath("$.username").value("hikingcarrot7"))
          .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
          .andExpect(jsonPath("$._links.self.href", is("http://localhost/users/hikingcarrot7")))
          .andExpect(jsonPath("$._links.piggy_banks.href", is("http://localhost")));
    }

    @Test
    void shouldReturn403_whenUsernameDifferentFromLoggedUser() throws Exception {
      doLogin();

      mockMvc.perform(
              get("/users/notFound")
                  .headers(authHeader())
          )
          .andExpect(status().isForbidden())
          .andExpect(jsonPath("$.detail").exists())
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

  }

  @Nested
  class UpdateUser {

    @Test
    void shouldOnlyUpdateUserPassword() throws Exception {
      doLogin();
      var updatedUser = Map.of("username", "newHikingCarrot", "password", "new-password123");

      mockMvc.perform(
              patch("/users/hikingcarrot7")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(updatedUser))
                  .headers(authHeader())
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.username").value("hikingcarrot7"))
          .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

      var updatedUserPassword = Map.of("username", "hikingcarrot7", "password", "new-password123");
      mockMvc.perform(
              post("/login")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(updatedUserPassword))
          )
          .andExpect(jsonPath("$.accessToken").exists())
          .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void shouldReturn403_whenUsernameDifferentFromLoggedUser() throws Exception {
      doLogin();
      var updatedUser = Map.of("username", "newHikingCarrot", "password", "new-password123");

      mockMvc.perform(
              patch("/users/notFound")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(updatedUser))
                  .headers(authHeader())
          )
          .andExpect(status().isForbidden())
          .andExpect(jsonPath("$.detail").exists())
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

  }

  @Nested
  class DeleteUser {

    @Test
    void shouldDeleteUser() throws Exception {
      doLogin();
      
      mockMvc
          .perform(
              delete("/users/hikingcarrot7")
                  .headers(authHeader())
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").exists())
          .andExpect(jsonPath("$.username").value("hikingcarrot7"))
          .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldReturn403_whenUsernameDifferentFromLoggedUser() throws Exception {
      doLogin();

      mockMvc.perform(
              delete("/users/notFound")
                  .headers(authHeader())
          )
          .andExpect(status().isForbidden())
          .andExpect(jsonPath("$.detail").exists())
          .andExpect(content().contentType(org.zalando.problem.spring.web.advice.MediaTypes.PROBLEM_VALUE));
    }

  }

}
