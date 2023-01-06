package com.cherrysoft.ahorrosapp.dtos;

import com.cherrysoft.ahorrosapp.dtos.validation.OnCreate;
import com.cherrysoft.ahorrosapp.dtos.validation.Password;
import com.cherrysoft.ahorrosapp.dtos.validation.Username;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@EqualsAndHashCode(callSuper = true)
@Relation(itemRelation = "user", collectionRelation = "users")
public class UserDTO extends RepresentationModel<UserDTO> {
  @Null
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private final Long id;

  @Username
  @NotBlank(groups = {OnCreate.class}, message = "A username is required.")
  private final String username;

  @Password
  @NotBlank(groups = {OnCreate.class}, message = "A password is required.")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private final String password;
}
