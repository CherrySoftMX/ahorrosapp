package com.cherrysoft.ahorrosapp.web.mappers;

import com.cherrysoft.ahorrosapp.common.core.models.User;
import com.cherrysoft.ahorrosapp.web.dtos.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User toUser(UserDTO dto);

  UserDTO toUserDto(User user);

}
