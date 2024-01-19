package com.example.network.dtos.comment;


import java.time.LocalDateTime;

public record CommentCreateRequestDto(

         String text,

         LocalDateTime timestamp

) {
}
