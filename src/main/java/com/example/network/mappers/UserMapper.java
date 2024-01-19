package com.example.network.mappers;

import com.example.network.dtos.UserDetailsDTO;
import com.example.network.dtos.user.UserCreateRequestDto;
import com.example.network.dtos.user.UserResponseDto;
import com.example.network.dtos.user.UserUpdateRequestDto;
import com.example.network.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntityCreate(UserCreateRequestDto userCreateRequestDto);

    UserCreateRequestDto toDtoCreate(User user);

    @Mapping(target = "id", ignore = true)
    User toEntityUpdate(UserUpdateRequestDto userUpdateRequestDto);

    UserResponseDto toResponseDto(User user);

    List<UserResponseDto> toResponseDtoList(List<User> userList);

    UserDetailsDTO toUserDatailsDTO(UserResponseDto userResponseDto);
}
