package com.example.network.dtos.user;

public record UserCreateRequestDto(

        String name,

        String username,

        String password,

        String email
) {
}
