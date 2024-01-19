package com.example.network.dtos.comment;

import com.example.network.entities.Post;
import com.example.network.entities.User;

import java.time.LocalDateTime;

public record CommentResponseDto(

        User user,

        Post post,

        String text,

        LocalDateTime timestamp
) {
}
