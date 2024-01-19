package com.example.network.dtos.comment;

import java.time.LocalDateTime;

public record CommentUpdateRequestDto(

        String text,

        LocalDateTime timestamp
) {
}
