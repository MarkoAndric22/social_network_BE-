package com.example.network.dtos.user;

import com.example.network.entities.enums.Role;

public record UserResponseDto(

        String name,

        String email,

        String username,

        Role role

) {
}
