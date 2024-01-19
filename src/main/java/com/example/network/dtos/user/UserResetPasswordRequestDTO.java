package com.example.network.dtos.user;

public record UserResetPasswordRequestDTO(

        String oldPassword,

        String newPassword
) {
}
