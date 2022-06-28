package com.cherrysoft.ahorrosapp.mappers;

import com.cherrysoft.ahorrosapp.dtos.UserDTO;
import com.cherrysoft.ahorrosapp.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User toUser(UserDTO dto);

  UserDTO toUserDto(User user);

}
