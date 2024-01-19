package com.example.network.dtos.post;


import java.time.LocalDateTime;

public record PostResponseDto(


        String text,

        LocalDateTime timestamp

) {
}
