package com.cherrysoft.ahorrosapp.web;


import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.cherrysoft.ahorrosapp.web.utils.JsonUtils;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractControllerIT {
  @Autowired protected MockMvc mockMvc;
  protected String accessToken;
  protected String refreshToken;

  protected void doLogin() throws Exception {
    User registeredUser = getUserForLogin();
    var body = Map.of("username", registeredUser.getUsername(), "password", registeredUser.getPassword());

    String response = mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJsonString(body))
        )
        .andReturn()
        .getResponse().getContentAsString();

    accessToken = JsonPath.read(response, "$.accessToken");
    refreshToken = JsonPath.read(response, "$.refreshToken");
  }

  protected User getUserForLogin() {
    return User.builder()
        .username("hikingcarrot7")
        .password("12345678")
        .build();
  }

  protected HttpHeaders authHeader() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Authorization", "Bearer " + accessToken);
    return httpHeaders;
  }

}
