package com.example.network.controllers;

import com.example.network.dtos.AuthenticationResponseDTO;
import com.example.network.dtos.LoginRequestDTO;
import com.example.network.dtos.user.UserCreateRequestDto;
import com.example.network.dtos.user.UserResetPasswordRequestDTO;
import com.example.network.dtos.user.UserResponseDto;
import com.example.network.dtos.user.UserUpdateRequestDto;
import com.example.network.entities.User;
import com.example.network.exceptions.AuthorizationCustomException;
import com.example.network.exceptions.NotFoundCustomException;
import com.example.network.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    private String createErrorMessage(BindingResult result) {
        return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
    }

    @RequestMapping(method = RequestMethod.POST,value = "/createUser")
    public UserResponseDto createUser(@RequestBody @Valid UserCreateRequestDto userCreateRequestDto){
        return userService.createUser(userCreateRequestDto);
    }

    @RequestMapping(method = RequestMethod.PUT,value = "/updateUser")
    public UserResponseDto updateUser(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto) throws NotFoundCustomException, AuthorizationCustomException {
        return userService.updateUser(userUpdateRequestDto);
    }

    @RequestMapping(method = RequestMethod.DELETE,value = "/deleteUser/{id}")
    public UserResponseDto deleteUser(@PathVariable Long id) throws NotFoundCustomException, AuthorizationCustomException {
        return userService.deleteUser(id);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public UserResponseDto getById(@PathVariable Long id) throws NotFoundCustomException, AuthorizationCustomException {
        return userService.getById(id);
    }
    @RequestMapping(method = RequestMethod.GET,value = "/getAll")
    public List<UserResponseDto> getAll() throws AuthorizationCustomException {
        return userService.getAll();
    }

    @RequestMapping(method = RequestMethod.GET,value = "/search")
    public List<User> search(@RequestParam String name){
        return userService.search(name);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    @ResponseBody
    public AuthenticationResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO
    ) throws NotFoundCustomException, AuthorizationCustomException {
        return userService.login(loginRequestDTO);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/forgot-password")
    @ResponseBody
    public UserResponseDto forgotPassword(@RequestParam String email) throws Exception {
       return userService.forgotPassword(email);
    }

    @RequestMapping(method = RequestMethod.POST, path = "reset-password")
    @ResponseBody
    public UserResponseDto resetPassword(@RequestParam String token, @RequestBody UserResetPasswordRequestDTO userResetPasswordRequestDTO) {
        UserResponseDto userResponseDto = userService.resetPassword(userResetPasswordRequestDTO.newPassword(), token);
        return userResponseDto;
    }
}
