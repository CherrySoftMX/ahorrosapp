package com.cherrysoft.ahorrosapp.controllers;

import com.cherrysoft.ahorrosapp.mappers.UserMapper;
import com.cherrysoft.ahorrosapp.mappers.UserMapperImpl;
import com.cherrysoft.ahorrosapp.models.User;
import com.cherrysoft.ahorrosapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static com.cherrysoft.ahorrosapp.controllers.utils.JsonUtils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(UserController.class)
@ContextConfiguration(classes = {UserMapperImpl.class})
class UserControllerTest {
  @MockBean private UserService userService;
  @Autowired private UserMapper userMapper;
  @Autowired private MockMvc mockMvc;

  @Test
  void whenUserIsValid_thenIsAddedToDatabase_and201StatusIsReturned() throws Exception {
    User providedUser = new User("hikingcarrot7", "123456");
    User addedUser = new User(1L, providedUser.getUsername(), providedUser.getPassword());
    String resourceLocation = String.format("%s/%d", UserController.BASE_URL, addedUser.getId());
    when(userService.addUser(any(User.class))).thenReturn(addedUser);

    mockMvc
        .perform(
            post(UserController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(providedUser))
        )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", resourceLocation))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void whenUserIsInvalid_then400StatusIsReturned() throws Exception {
    User providedUser = new User("hik", "1234");

    mockMvc
        .perform(
            post(UserController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(providedUser))
        )
        .andDo(print())
        .andExpect(status().isBadRequest());

    verify(userService, never()).addUser(any(User.class));
  }

  @Test
  void whenUserIsDeleted_then200StatusIsReturned() throws Exception {
    User userToDelete = new User(1L, "hikingcarrot7", "123456");
    when(userService.deleteUser(userToDelete.getUsername())).thenReturn(userToDelete);

    mockMvc
        .perform(
            delete(String.format("%s/%s", UserController.BASE_URL, userToDelete.getUsername()))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value(userToDelete.getUsername()))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

}