package com.example.network.services;

import com.example.network.dtos.AuthenticationResponseDTO;
import com.example.network.dtos.LoginRequestDTO;
import com.example.network.dtos.user.UserCreateRequestDto;
import com.example.network.dtos.user.UserResponseDto;
import com.example.network.dtos.user.UserUpdateRequestDto;
import com.example.network.entities.User;
import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.exceptions.NotFoundCustomException;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserCreateRequestDto userCreateRequestDto);

    UserResponseDto updateUser(UserUpdateRequestDto userUpdateRequestDto) throws AuthorizationCustomException;

    UserResponseDto deleteUser(Long id) throws NotFoundCustomException, AuthorizationCustomException;

    UserResponseDto getById(Long id) throws NotFoundCustomException, AuthorizationCustomException;

    List<UserResponseDto> getAll() throws AuthorizationCustomException;

    List<User> search(String name);

    AuthenticationResponseDTO login(LoginRequestDTO loginRequestDTO) throws NotFoundCustomException, AuthorizationCustomException;

    UserResponseDto forgotPassword(String email) throws Exception;

    UserResponseDto resetPassword(String password, String token);
}
