package ru.oleevych.cloudspace.mapper;

import org.mapstruct.Mapper;
import ru.oleevych.cloudspace.dto.UserRegisterDto;
import ru.oleevych.cloudspace.entity.User;

@Mapper(componentModel = "spring")
public interface UserRegisterMapper {
    User toUser(UserRegisterDto dto);
}
