package com.example.network.dtos.user;

public record UserUpdateRequestDto(

        String name,

        String username,

        String password,

        String email
) {
}
