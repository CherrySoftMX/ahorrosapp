package com.cherrysoft.ahorrosapp.web.controllers;

import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.cherrysoft.ahorrosapp.common.repositories.UserRepository;
import com.cherrysoft.ahorrosapp.common.services.UserService;
import com.cherrysoft.ahorrosapp.web.AbstractControllerIT;
import com.cherrysoft.ahorrosapp.web.utils.JsonUtils;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginAndRegistrationControllerIT extends AbstractControllerIT {
  @Autowired private UserService userService;
  @Autowired private UserRepository userRepository;
  @Autowired private JwtDecoder accessTokenDecoder;
  @Autowired @Qualifier("jwtRefreshTokenDecoder") private JwtDecoder refreshTokenDecoder;
  @MockBean(name = "tokenEncoderClock") private Clock clock;
  private User user;

  @BeforeEach
  void init() {
    user = User.builder()
        .username("hikingcarrot7")
        .password("password12345")
        .build();
    userService.createUser(user);

    Clock defaultClock = Clock.systemDefaultZone();
    given(clock.instant()).willReturn(defaultClock.instant());
    given(clock.getZone()).willReturn(defaultClock.getZone());
  }

  @AfterEach
  void deleteUsers() {
    userRepository.deleteAll();
  }

  @Nested
  class Login {

    @Test
    void shouldLogin_whenValidCredentials() throws Exception {
      String response = mockMvc.perform(
              post("/login")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(user))
          )
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.username").value("hikingcarrot7"))
          .andExpect(jsonPath("$.accessToken").exists())
          .andExpect(jsonPath("$.refreshToken").exists())
          .andReturn()
          .getResponse().getContentAsString();

      String accessToken = JsonPath.read(response, "$.accessToken");
      String refreshToken = JsonPath.read(response, "$.refreshToken");

      assertValidAccessToken(accessToken);
      assertValidRefreshToken(refreshToken);
    }

    @Test
    void shouldReturn404_whenUsernameNotFound() throws Exception {
      User invalidUser = new User("invalidUsername", "password12345");

      mockMvc.perform(
              post("/login")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(invalidUser)))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    void shouldReturn401_whenIncorrectPassword() throws Exception {
      User userWithInvalidPassword = new User("hikingcarrot7", "invalid-password");

      mockMvc.perform(
              post("/login")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(userWithInvalidPassword))
          )
          .andExpect(status().isUnauthorized())
          .andExpect(jsonPath("$.detail").exists());
    }

  }

  @Nested
  class Register {

    @Test
    void shouldRegister_whenValidCredentials() throws Exception {
      User newUser = new User("newHikingCarrot", "password12345");

      String response = mockMvc.perform(
              post("/register")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(newUser))
          )
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.username").value("newHikingCarrot"))
          .andExpect(jsonPath("$.accessToken").exists())
          .andExpect(jsonPath("$.refreshToken").exists())
          .andReturn()
          .getResponse().getContentAsString();

      String accessToken = JsonPath.read(response, "$.accessToken");
      String refreshToken = JsonPath.read(response, "$.refreshToken");

      assertValidAccessToken(accessToken);
      assertValidRefreshToken(refreshToken);
    }

    @Test
    void shouldReturn400_whenUsernameAlreadyTaken() throws Exception {
      mockMvc.perform(
              post("/register")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(user))
          )
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    void shouldReturn400_whenUsernameIsLess5CharsOrMore20Chars() throws Exception {
      User userUsernameLess5Chars = new User("hik", "password12345");
      mockMvc.perform(
              post("/register")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(userUsernameLess5Chars))
          )
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.fieldErrors.username").exists());

      User userUsernameMore20Chars = new User("newSuperLargeUsernameMore20Chars", "password12345");
      mockMvc.perform(
              post("/register")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(userUsernameMore20Chars))
          )
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.fieldErrors.username").exists());
    }

    @Test
    void shouldReturn400_whenUsernameHasSpecialChars() throws Exception {
      User userUsernameSpecialChars = new User("@_:BatChest:!?", "password12345");

      mockMvc.perform(
              post("/register")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(userUsernameSpecialChars))
          )
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.fieldErrors.username").exists());
    }

    @Test
    void shouldReturn400_whenPasswordLess6CharsOrMore16Chars() throws Exception {
      User userPasswordLess6Chars = new User("hikingcarrot7", "passw");
      mockMvc.perform(
              post("/register")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(userPasswordLess6Chars))
          )
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.fieldErrors.password").exists());

      User userPasswordMore16Chars = new User("hikingcarrrot7", "very-very-very-large-password");
      mockMvc.perform(
              post("/register")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(userPasswordMore16Chars))
          )
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.fieldErrors.password").exists());
    }

  }

  @Nested
  class RefreshToken {

    @Test
    void shouldReturnNewAccessToken_andKeepSameRefreshToken() throws Exception {
      doLogin();
      Map<String, String> body = Map.of("refreshToken", refreshToken);
      // This cost me 2 hours of debugging. LOL
      // It seems that, after logging, we must wait at least 1 second
      // before calling the "refresh-token" endpoint.
      given(clock.instant()).willReturn(Instant.now().plusSeconds(1));
      given(clock.getZone()).willReturn(Clock.systemDefaultZone().getZone());

      String response = mockMvc.perform(
              post("/refresh-token")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(body))
          )
          .andExpect(status().isOk())
          .andReturn()
          .getResponse().getContentAsString();

      String newAccessToken = JsonPath.read(response, "$.accessToken");
      String responseRefreshToken = JsonPath.read(response, "$.refreshToken");

      assertValidAccessToken(newAccessToken);
      assertThat(newAccessToken).isNotEqualTo(accessToken);

      assertValidRefreshToken(responseRefreshToken);
      assertThat(responseRefreshToken).isEqualTo(refreshToken);
    }

    @Test
    void shouldReturnNewRefreshToken_whenLessThan1WeekBeforeExpiration() throws Exception {
      doLogin();
      // Travel 6 days to the future. Refresh token has 4 more days before expiration
      LocalDate timeToTheFuture = LocalDate.now().plusDays(6);
      ZoneId zoneId = ZoneId.systemDefault();
      given(clock.instant()).willReturn(timeToTheFuture.atStartOfDay(zoneId).toInstant());
      given(clock.getZone()).willReturn(zoneId);
      Map<String, String> body = Map.of("refreshToken", refreshToken);

      String response = mockMvc.perform(
              post("/refresh-token")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(JsonUtils.asJsonString(body))
          )
          .andDo(print())
          .andExpect(status().isOk())
          .andReturn()
          .getResponse().getContentAsString();

      String newAccessToken = JsonPath.read(response, "$.accessToken");
      String responseRefreshToken = JsonPath.read(response, "$.refreshToken");

      assertValidAccessToken(newAccessToken);
      assertThat(newAccessToken).isNotEqualTo(accessToken);

      assertValidRefreshToken(responseRefreshToken);
      assertThat(responseRefreshToken).isNotEqualTo(refreshToken);
    }

  }

  private void assertValidAccessToken(String token) {
    assertValidToken(token, accessTokenDecoder);
  }

  private void assertValidRefreshToken(String token) {
    assertValidToken(token, refreshTokenDecoder);
  }

  private void assertValidToken(String token, JwtDecoder decoder) {
    try {
      decoder.decode(token);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected User getUserForLogin() {
    return user;
  }

}