package com.cherrysoft.ahorrosapp.mappers;

import com.cherrysoft.ahorrosapp.core.models.User;
import com.cherrysoft.ahorrosapp.dtos.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User toUser(UserDTO dto);

  UserDTO toUserDto(User user);

}
